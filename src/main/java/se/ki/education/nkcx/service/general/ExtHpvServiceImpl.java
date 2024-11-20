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
import se.ki.education.nkcx.util.excelExport.PersonExtHpvExcelExporter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExtHpvServiceImpl implements ExtHpvService{

    private static final Logger LOG = LogManager.getLogger();

    private final ExtHpvRepo extHpvRepo;

    private LogService logService;

    private final DtoUtil<ExtHpvEntity, ExtHpvReq, ExtHpvRes> dtoUtil;

    private final DtoUtil<PersonExtHpvEntity, PersonExtHpvReq, PersonExtHpvRes> personExtHpvEntityDtoUtil;

    private final PersonRepo personRepo;

    private final PaginationDtoUtil<ExtHpvEntity, ExtHpvReq, ExtHpvRes> paginationDtoUtil;
    private final PersonExtHpvRepo personExtHpvRepo;

    public ExtHpvServiceImpl(ExtHpvRepo extHpvRepo, LogService logService, DtoUtil<ExtHpvEntity, ExtHpvReq, ExtHpvRes> dtoUtil, DtoUtil<PersonExtHpvEntity, PersonExtHpvReq, PersonExtHpvRes> personExtHpvEntityDtoUtil, PersonRepo personRepo, PaginationDtoUtil<ExtHpvEntity, ExtHpvReq, ExtHpvRes> paginationDtoUtil, PersonExtHpvRepo personExtHpvRepo) {
        this.extHpvRepo = extHpvRepo;
        this.logService = logService;
        this.dtoUtil = dtoUtil;
        this.personExtHpvEntityDtoUtil = personExtHpvEntityDtoUtil;
        this.personRepo = personRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.personExtHpvRepo = personExtHpvRepo;
    }

    @PreAuthorize("hasAuthority('EXTHPV_C')")
    @Override
    public ExtHpvRes save(ExtHpvReq extHpvReq) {
        LOG.info("----- Saving ExtHpv. -----");
        extHpvRepo.findByName(extHpvReq.getName()).ifPresent(extHpvEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.EXTHPV002.getMessage());
        });

        //save ExtHpv without person id
        ExtHpvEntity entity = dtoUtil.reqToEntity(extHpvReq);
        ExtHpvEntity savedEntity =  extHpvRepo.save(entity);

        //fetch personEntity
        PersonEntity personEntity = personRepo.findById(extHpvReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.PERSON002.getMessage()));

        //save personExtHpv with person_id and extHpv_id
        PersonExtHpvEntity personExtHpvEntity = new PersonExtHpvEntity();
        personExtHpvEntity.setPersonEntity(personEntity);
        personExtHpvEntity.setExtHpvEntity(savedEntity);
        personExtHpvRepo.save(personExtHpvEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new ExtHpv", "A new ExtHpv data is added successfully.", currentUser);

        return  dtoUtil.prepRes(savedEntity);
    }

    @PreAuthorize("hasAnyAuthority('ExtHPV_RA')")
    @Override
    public PaginationRes<ExtHpvRes> get(PaginationReq paginationReq) {
        LOG.info("------- Getting ExtHpv details. --------------");

        List<String> fields = Arrays.asList("name"); // Fields to be returned in the response
        String sortBy = "name"; // Default sort by field
        Sort.Direction sortOrder = Sort.Direction.ASC; // Default sort order

        // Perform pagination and mapping
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, extHpvRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('EXTHPV_U')")
    @Override
    @Transactional
    public ExtHpvRes update(ExtHpvReq extHpvReq) {
        LOG.info("----- Updating ExtHpv. -----");

        // Fetch existing ExtHpv entity
        ExtHpvEntity existingExtHpv = extHpvRepo.findById(extHpvReq.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ExtHpv not found"));

        // Update the ExtHpv entity
        existingExtHpv.setName(extHpvReq.getName());
        ExtHpvEntity updatedExtHpv = extHpvRepo.save(existingExtHpv);

        // Fetch the PersonEntity
        PersonEntity personEntity = personRepo.findById(extHpvReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

        // Find the existing PersonExtHpvEntity
        PersonExtHpvEntity personExtHpvEntity = personExtHpvRepo.findByExtHpvEntityAndPersonEntity(updatedExtHpv, personEntity);

        if (personExtHpvEntity != null) {
            // Log before updating
            LOG.debug("Updating existing personExtHpvEntity: " + personExtHpvEntity);

            // Update existing PersonExtHpvEntity
            personExtHpvEntity.setExtHpvEntity(updatedExtHpv);
            personExtHpvEntity = personExtHpvRepo.save(personExtHpvEntity); // Ensure changes are saved

            LOG.debug("Updated PersonExtHpvEntity: " + personExtHpvEntity);
        } else {
            // Log if no entry found
            LOG.warn("No existing PersonExtHpvEntity found for ExtHpvEntity ID: " + updatedExtHpv.getId() + " and PersonEntity ID: " + personEntity.getId());
        }

        // Log the activity
        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing ExtHpv data", "Existing ExtHpv data is modified successfully. ",  currentUser);

        return dtoUtil.prepRes(updatedExtHpv);
    }

    @PreAuthorize("hasAuthority('EXTHPV_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting ExtHpv. -----");
        Optional<ExtHpvEntity> optionalExtHpvEntity = extHpvRepo.findById(id);
        optionalExtHpvEntity.orElseThrow(() -> new CustomException("COU001"));
        extHpvRepo.delete(optionalExtHpvEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing ExtHpv", "Existing ExtHpv data is deleted successfully. "  ,  currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Person ExtHpv Data. ------");

        List<ExtHpvReq> extHpvReqs = extractDataFromFile(multipartFile.getInputStream());

        List<ExtHpvEntity> extHpvEntities = new ArrayList<>();
        List<PersonExtHpvEntity> personExtHpvEntities = new ArrayList<>();

        for (ExtHpvReq extHpvReq : extHpvReqs) {
            ExtHpvEntity extHpvEntity = dtoUtil.reqToEntity(extHpvReq);
            ExtHpvEntity savedExtHpv = extHpvRepo.save(extHpvEntity);
            extHpvEntities.add(savedExtHpv);

            PersonEntity personEntity = personRepo.findById(extHpvReq.getPersonId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

            PersonExtHpvEntity personExtHpvEntity = new PersonExtHpvEntity();
            personExtHpvEntity.setPersonEntity(personEntity);
            personExtHpvEntity.setExtHpvEntity(savedExtHpv);
            personExtHpvEntities.add(personExtHpvEntity);
        }

        // Save all PersonExtHpvEntity instances
        personExtHpvRepo.saveAll(personExtHpvEntities);

        return !extHpvEntities.isEmpty() && !personExtHpvEntities.isEmpty();
    }

    private List<ExtHpvReq> extractDataFromFile(InputStream inputStream) throws IOException{
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<ExtHpvReq> extHpvReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            ExtHpvReq extHpvReq = extractDataFromRow(row);
            extHpvReqs.add(extHpvReq);
        }

        workbook.close();
        return extHpvReqs;
    }

    private ExtHpvReq extractDataFromRow(Row row) {
        ExtHpvReq extHpvReq = new ExtHpvReq();
//        PersonExtHpvReq personExtHpvReq = new PersonExtHpvReq();

        if (row != null) {
            // Assuming the Hpv data is in columns 0 and 1
            Long personId = DataValidationForExcelImport.getLongCellValueOrDefault(row, 0);
            String extHpvName = DataValidationForExcelImport.getCellValueOrDefault(row, 1);

            extHpvReq.setPersonId(personId);
            extHpvReq.setName(extHpvName);
        }

        return extHpvReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "id";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        PersonExtHpvExcelExporter exportPersonExtHpvAllData = new PersonExtHpvExcelExporter();
        List<PersonExtHpvEntity> personExtHpvEntities = personExtHpvRepo.findAll( Sort.by(sortOrder, sortBy));
        List<PersonExtHpvRes> feedbackResDtos = personExtHpvEntities.stream().map(personExtHpvEntityDtoUtil::prepRes)
                .collect(Collectors.toList());
        return exportPersonExtHpvAllData.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<ExtHpvRes> findExtHpvByName(String name) {
        return extHpvRepo.findByExtHpvContaining(name)
                .stream()
                .map(extHpvEntity -> {
                    ExtHpvRes extHpvRes = new ExtHpvRes();
                    extHpvRes.setId(extHpvEntity.getId());
                    extHpvRes.setName(extHpvEntity.getName());

                    // Find the associated PersonExtHpvResEntity using the cellEntityId
                    List<PersonExtHpvEntity> personExtHpvEntityList = personExtHpvRepo.findByExtHpvEntityId(extHpvEntity.getId());
                    if (!personExtHpvEntityList.isEmpty()) {

                        PersonExtHpvEntity personExtHpvEntity = personExtHpvEntityList.get(0);
                        extHpvRes.setPersonId(personExtHpvEntity.getPersonEntity().getId());
                    }

                    return extHpvRes;
                })
                .collect(Collectors.toList());
    }
}
