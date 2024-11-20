package se.ki.education.nkcx.service.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import se.ki.education.nkcx.dto.request.*;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.*;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.*;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.PersonCellExcelExporter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CellServiceImpl implements CellService {

    private static final Logger LOG = LogManager.getLogger();

    private final CellRepo cellRepo;

    private LogService logService;

    private final DtoUtil<CellEntity, CellReq, CellRes> dtoUtil;

    private final DtoUtil<PersonCellEntity, PersonCellReq, PersonCellRes> personCellEntityDtoUtil;

    private final PersonRepo personRepo;

    private final PaginationDtoUtil<CellEntity, CellReq, CellRes> paginationDtoUtil;
    private final PersonCellRepo personCellRepo;

    public CellServiceImpl(CellRepo cellRepo, LogService logService, DtoUtil<CellEntity, CellReq, CellRes> dtoUtil, DtoUtil<PersonCellEntity, PersonCellReq, PersonCellRes> personCellEntityDtoUtil, PersonRepo personRepo, PaginationDtoUtil<CellEntity, CellReq, CellRes> paginationDtoUtil, PersonCellRepo personCellRepo) {
        this.cellRepo = cellRepo;
        this.logService = logService;
        this.dtoUtil = dtoUtil;
        this.personCellEntityDtoUtil = personCellEntityDtoUtil;
        this.personRepo = personRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.personCellRepo = personCellRepo;
    }


    @PreAuthorize("hasAuthority('CELL_C')")
    @Override
    public CellRes save(CellReq cellReq) {
        LOG.info("----- Saving Cell. -----");
        cellRepo.findByName(cellReq.getName()).ifPresent(cellEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.CELL002.getMessage());
        });

        //save Cell without person id
        CellEntity entity = dtoUtil.reqToEntity(cellReq);
        CellEntity savedEntity =  cellRepo.save(entity);

        //fetch personEntity
        PersonEntity personEntity = personRepo.findById(cellReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.PERSON002.getMessage()));

        //save personCell with person_id and cell_id
        PersonCellEntity personCellEntity = new PersonCellEntity();
        personCellEntity.setPersonEntity(personEntity);
        personCellEntity.setCellEntity(savedEntity);
        personCellRepo.save(personCellEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Cell", "A new Cell data is added successfully.", currentUser);

        return  dtoUtil.prepRes(savedEntity);
    }

    @PreAuthorize("hasAnyAuthority('CELL_RA')")
    @Override
    public PaginationRes<CellRes> get(PaginationReq paginationReq) {
        LOG.info("------- Getting Cell details. --------------");

        List<String> fields = Arrays.asList("name"); // Fields to be returned in the response
        String sortBy = "name"; // Default sort by field
        Sort.Direction sortOrder = Sort.Direction.ASC; // Default sort order

        // Perform pagination and mapping
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, cellRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('CELL_U')")
    @Override
    @Transactional
    public CellRes update(CellReq cellReq) {
        LOG.info("----- Updating Cell. -----");

        // Fetch existing Cell entity
        CellEntity existingCell = cellRepo.findById(cellReq.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cell not found"));

        // Update the Cell entity
        existingCell.setName(cellReq.getName());
        CellEntity updatedCell = cellRepo.save(existingCell);

        // Fetch the PersonEntity
        PersonEntity personEntity = personRepo.findById(cellReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

        // Find the existing PersonCellEntity
        PersonCellEntity personCellEntity = personCellRepo.findByCellEntityAndPersonEntity(updatedCell, personEntity);

        if (personCellEntity != null) {
            // Log before updating
            LOG.debug("Updating existing PersonCellEntity: " + personCellEntity);

            // Update existing PersonCellEntity
            personCellEntity.setCellEntity(updatedCell);
            personCellEntity = personCellRepo.save(personCellEntity); // Ensure changes are saved

            LOG.debug("Updated PersonCellEntity: " + personCellEntity);
        } else {
            // Log if no entry found
            LOG.warn("No existing PersonCellEntity found for CellEntity ID: " + updatedCell.getId() + " and PersonEntity ID: " + personEntity.getId());
        }

        // Log the activity
        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing Cell data", "Existing Cell data is modified successfully. ",  currentUser);

        return dtoUtil.prepRes(updatedCell);
    }

    @PreAuthorize("hasAuthority('CELL_D')")
    @Override
    public void delete(Long id)  {
        LOG.info("----- Deleting Cell. -----");
        Optional<CellEntity> optionalCellEntity = cellRepo.findById(id);
        optionalCellEntity.orElseThrow(() -> new CustomException("COU001"));
        cellRepo.delete(optionalCellEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Cell", "Existing Cell data is deleted successfully. "  ,  currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Person Cell Data. ------");

        List<CellReq> cellReqs = extractDataFromFile(multipartFile.getInputStream());

        List<CellEntity> cellEntities = new ArrayList<>();
        List<PersonCellEntity> personCellEntities = new ArrayList<>();

        for (CellReq cellReq : cellReqs) {
            CellEntity cellEntity = dtoUtil.reqToEntity(cellReq);
            CellEntity savedCell = cellRepo.save(cellEntity);
            cellEntities.add(savedCell);

            PersonEntity personEntity = personRepo.findById(cellReq.getPersonId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

            PersonCellEntity personCellEntity = new PersonCellEntity();
            personCellEntity.setPersonEntity(personEntity);
            personCellEntity.setCellEntity(savedCell);
            personCellEntities.add(personCellEntity);
        }

        // Save all PersonCellEntity instances
        personCellRepo.saveAll(personCellEntities);

        return !cellEntities.isEmpty() && !personCellEntities.isEmpty();
    }

    private List<CellReq> extractDataFromFile(InputStream inputStream) throws IOException{
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<CellReq> cellReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            CellReq cellReq = extractDataFromRow(row);
            cellReqs.add(cellReq);
        }

        workbook.close();
        return cellReqs;
    }

    private CellReq extractDataFromRow(Row row) {
        CellReq cellReq = new CellReq();

        if (row != null) {
            // Assuming the Cell data is in columns 0 and 1
            Long personId = DataValidationForExcelImport.getLongCellValueOrDefault(row, 0);
            String cellName = DataValidationForExcelImport.getCellValueOrDefault(row, 1);

            cellReq.setPersonId(personId);
            cellReq.setName(cellName);
        }

        return cellReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "id";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        PersonCellExcelExporter exportPersonCellAllData = new PersonCellExcelExporter();
        List<PersonCellEntity> personCellEntities = personCellRepo.findAll( Sort.by(sortOrder, sortBy));
        List<PersonCellRes> feedbackResDtos = personCellEntities.stream().map(personCellEntityDtoUtil::prepRes)
                .collect(Collectors.toList());
        return exportPersonCellAllData.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<CellRes> findCellByName(String name) {
        return cellRepo.findByCellContaining(name)
                .stream()
                .map(cellEntity -> {
                    CellRes cellRes = new CellRes();
                    cellRes.setId(cellEntity.getId());
                    cellRes.setName(cellEntity.getName());

                    // Find the associated PersonCellResEntity using the cellEntityId
                    List<PersonCellEntity> personCellEntityList = personCellRepo.findByCellEntityId(cellEntity.getId());
                    if (!personCellEntityList.isEmpty()) {
                        // Assuming you want to use the first associated PersonCellEntity
                        PersonCellEntity personCellEntity = personCellEntityList.get(0);
                        cellRes.setPersonId(personCellEntity.getPersonEntity().getId());
                    }

                    return cellRes;
                })
                .collect(Collectors.toList());
    }


}

