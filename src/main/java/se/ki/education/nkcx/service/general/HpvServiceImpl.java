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
import se.ki.education.nkcx.util.excelExport.PersonHpvExcelExporter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HpvServiceImpl implements HpvService {

    private static final Logger LOG = LogManager.getLogger();

    private final HpvRepo hpvRepo;

    private LogService logService;

    private final DtoUtil<HpvEntity, HpvReq, HpvRes> dtoUtil;

    private final DtoUtil<PersonHpvEntity, PersonHpvReq, PersonHpvRes> personHpvEntityDtoUtil;

    private final PersonRepo personRepo;

    private final PaginationDtoUtil<HpvEntity, HpvReq, HpvRes> paginationDtoUtil;
    private final PersonHpvRepo personHpvRepo;

    public HpvServiceImpl(HpvRepo hpvRepo, LogService logService, DtoUtil<HpvEntity, HpvReq, HpvRes> dtoUtil, DtoUtil<PersonHpvEntity, PersonHpvReq, PersonHpvRes> personHpvEntityDtoUtil, PersonRepo personRepo, PaginationDtoUtil<HpvEntity, HpvReq, HpvRes> paginationDtoUtil, PersonHpvRepo personHpvRepo) {
        this.hpvRepo = hpvRepo;
        this.logService = logService;
        this.dtoUtil = dtoUtil;
        this.personHpvEntityDtoUtil = personHpvEntityDtoUtil;
        this.personRepo = personRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.personHpvRepo = personHpvRepo;
    }


    @PreAuthorize("hasAuthority('HPV_C')")
    @Override
    public HpvRes save(HpvReq hpvReq) {
        LOG.info("----- Saving Hpv. -----");
        hpvRepo.findByName(hpvReq.getName()).ifPresent(hpvEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.HPV002.getMessage());
        });

        //save Hpv without person id
        HpvEntity entity = dtoUtil.reqToEntity(hpvReq);
        HpvEntity savedEntity =  hpvRepo.save(entity);

        //fetch personEntity
        PersonEntity personEntity = personRepo.findById(hpvReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.PERSON002.getMessage()));

        //save personHpv with person_id and hpv_id
        PersonHpvEntity personHpvEntity = new PersonHpvEntity();
        personHpvEntity.setPersonEntity(personEntity);
        personHpvEntity.setHpvEntity(savedEntity);
        personHpvRepo.save(personHpvEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Hpv", "A new Hpv data is added successfully.", currentUser);

        return  dtoUtil.prepRes(savedEntity);
    }

    @PreAuthorize("hasAnyAuthority('HPV_RA')")
    @Override
    public PaginationRes<HpvRes> get(PaginationReq paginationReq) {
        LOG.info("------- Getting Hpv details. --------------");

        List<String> fields = Arrays.asList("name"); // Fields to be returned in the response
        String sortBy = "name"; // Default sort by field
        Sort.Direction sortOrder = Sort.Direction.ASC; // Default sort order

        // Perform pagination and mapping
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, hpvRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('HPV_U')")
    @Override
    @Transactional
    public HpvRes update(HpvReq hpvReq) {
        LOG.info("----- Updating Hpv. -----");

        // Fetch existing Hpv entity
        HpvEntity existingHpv = hpvRepo.findById(hpvReq.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hpv not found"));

        // Update the Hpv entity
        existingHpv.setName(hpvReq.getName());
        HpvEntity updatedHpv = hpvRepo.save(existingHpv);

        // Fetch the PersonEntity
        PersonEntity personEntity = personRepo.findById(hpvReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

        // Find the existing PersonHpvEntity
        PersonHpvEntity personHpvEntity = personHpvRepo.findByHpvEntityAndPersonEntity(updatedHpv, personEntity);

        if (personHpvEntity != null) {
            // Log before updating
            LOG.debug("Updating existing PersonHpvEntity: " + personHpvEntity);

            // Update existing PersonHpvEntity
            personHpvEntity.setHpvEntity(updatedHpv);
            personHpvEntity = personHpvRepo.save(personHpvEntity); // Ensure changes are saved

            LOG.debug("Updated PersonHpvEntity: " + personHpvEntity);
        } else {
            // Log if no entry found
            LOG.warn("No existing PersonHpvEntity found for HpvEntity ID: " + updatedHpv.getId() + " and PersonEntity ID: " + personEntity.getId());
        }

        // Log the activity
        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing Hpv data", "Existing Hpv data is modified successfully. ",  currentUser);

        return dtoUtil.prepRes(updatedHpv);
    }

    @PreAuthorize("hasAuthority('HPV_D')")
    @Override
    public void delete(Long id)  {
        LOG.info("----- Deleting Hpv. -----");
        Optional<HpvEntity> optionalHpvEntity = hpvRepo.findById(id);
        optionalHpvEntity.orElseThrow(() -> new CustomException("COU001"));
        hpvRepo.delete(optionalHpvEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Hpv", "Existing Hpv data is deleted successfully. "  ,  currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Person Hpv Data. ------");

        List<HpvReq> hpvReqs = extractDataFromFile(multipartFile.getInputStream());

        List<HpvEntity> hpvEntities = new ArrayList<>();
        List<PersonHpvEntity> personHpvEntities = new ArrayList<>();

        for (HpvReq hpvReq : hpvReqs) {
            HpvEntity hpvEntity = dtoUtil.reqToEntity(hpvReq);
            HpvEntity savedHpv = hpvRepo.save(hpvEntity);
            hpvEntities.add(savedHpv);

            PersonEntity personEntity = personRepo.findById(hpvReq.getPersonId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

            PersonHpvEntity personHpvEntity = new PersonHpvEntity();
            personHpvEntity.setPersonEntity(personEntity);
            personHpvEntity.setHpvEntity(savedHpv);
            personHpvEntities.add(personHpvEntity);
        }

        // Save all PersonHpvEntity instances
        personHpvRepo.saveAll(personHpvEntities);

        return !hpvEntities.isEmpty() && !personHpvEntities.isEmpty();
    }

    private List<HpvReq> extractDataFromFile(InputStream inputStream) throws IOException{
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<HpvReq> hpvReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            HpvReq hpvReq = extractDataFromRow(row);
            hpvReqs.add(hpvReq);
        }

        workbook.close();
        return hpvReqs;
    }

    private HpvReq extractDataFromRow(Row row) {
        HpvReq hpvReq = new HpvReq();
//        PersonHpvReq personHpvReq = new PersonHpvReq();

        if (row != null) {
            // Assuming the Hpv data is in columns 0 and 1
            Long personId = DataValidationForExcelImport.getLongCellValueOrDefault(row, 0);
            String hpvName = DataValidationForExcelImport.getCellValueOrDefault(row, 1);

            hpvReq.setPersonId(personId);
            hpvReq.setName(hpvName);
        }

        return hpvReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "id";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        PersonHpvExcelExporter exportPersonHpvAllData = new PersonHpvExcelExporter();
        List<PersonHpvEntity> personHpvEntities = personHpvRepo.findAll( Sort.by(sortOrder, sortBy));
        List<PersonHpvRes> feedbackResDtos = personHpvEntities.stream().map(personHpvEntityDtoUtil::prepRes)
                .collect(Collectors.toList());
        return exportPersonHpvAllData.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<HpvRes> findHpvByName(String name) {
        return hpvRepo.findByHpvContaining(name)
                .stream()
                .map(hpvEntity -> {
                    HpvRes hpvRes = new HpvRes();
                    hpvRes.setId(hpvEntity.getId());
                    hpvRes.setName(hpvEntity.getName());

                    // Find the associated PersonHpvResEntity using the cellEntityId
                    List<PersonHpvEntity> personHpvEntityList = personHpvRepo.findByHpvEntityId(hpvEntity.getId());
                    if (!personHpvEntityList.isEmpty()) {
                        // Assuming you want to use the first associated PersonCellEntity
                        PersonHpvEntity personHpvEntity = personHpvEntityList.get(0);
                        hpvRes.setPersonId(personHpvEntity.getPersonEntity().getId());
                    }

                    return hpvRes;
                })
                .collect(Collectors.toList());
    }


}

