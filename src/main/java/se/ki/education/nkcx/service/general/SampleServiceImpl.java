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
import se.ki.education.nkcx.util.excelExport.PersonSampleExcelExporter;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SampleServiceImpl implements SampleService {

    private static final Logger LOG = LogManager.getLogger();

    private final SampleRepo sampleRepo;

    private LogService logService;

    private final DtoUtil<SampleEntity, SampleReq, SampleRes> dtoUtil;

    private final DtoUtil<PersonSampleEntity, PersonSampleReq, PersonSampleRes> personSampleEntityDtoUtil;

    private final PersonRepo personRepo;

    private final PaginationDtoUtil<SampleEntity, SampleReq, SampleRes> paginationDtoUtil;
    private final PersonSampleRepo personSampleRepo;

    public SampleServiceImpl(SampleRepo sampleRepo, LogService logService, DtoUtil<SampleEntity, SampleReq, SampleRes> dtoUtil, DtoUtil<PersonSampleEntity, PersonSampleReq, PersonSampleRes> personSampleEntityDtoUtil, PersonRepo personRepo, PaginationDtoUtil<SampleEntity, SampleReq, SampleRes> paginationDtoUtil, PersonSampleRepo personSampleRepo) {
        this.sampleRepo = sampleRepo;
        this.logService = logService;
        this.dtoUtil = dtoUtil;
        this.personSampleEntityDtoUtil = personSampleEntityDtoUtil;
        this.personRepo = personRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.personSampleRepo = personSampleRepo;
    }


    @PreAuthorize("hasAuthority('SAMPLE_C')")
    @Override
    public SampleRes save(SampleReq sampleReq) {
        LOG.info("----- Saving Sample. -----");
        sampleRepo.findByType(sampleReq.getType()).ifPresent(sampleEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.SAMPLE002.getMessage());
        });

        //save Sample without person id
        SampleEntity entity = dtoUtil.reqToEntity(sampleReq);
        SampleEntity savedEntity =  sampleRepo.save(entity);

        //fetch personEntity
        PersonEntity personEntity = personRepo.findById(sampleReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.PERSON002.getMessage()));

        //save personSample with person_id and sample_id
        PersonSampleEntity personSampleEntity = new PersonSampleEntity();
        personSampleEntity.setPersonEntity(personEntity);
        personSampleEntity.setSampleEntity(savedEntity);
        personSampleRepo.save(personSampleEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Sample", "A new Sample data is added successfully.", currentUser);

        return  dtoUtil.prepRes(savedEntity);
    }

    @PreAuthorize("hasAnyAuthority('SAMPLE_RA')")
    @Override
    public PaginationRes<SampleRes> get(PaginationReq paginationReq) {
        LOG.info("------- Getting Sample details. --------------");

        List<String> fields = Arrays.asList("name"); // Fields to be returned in the response
        String sortBy = "name"; // Default sort by field
        Sort.Direction sortOrder = Sort.Direction.ASC; // Default sort order

        // Perform pagination and mapping
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, sampleRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('SAMPLE_U')")
    @Override
    @Transactional
    public SampleRes update(SampleReq sampleReq) {
        LOG.info("----- Updating Sample. -----");

        // Fetch existing Sample entity
        SampleEntity existingSample = sampleRepo.findById(sampleReq.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sample not found"));

        // Update the Sample entity
        existingSample.setType(sampleReq.getType());
        SampleEntity updatedSample = sampleRepo.save(existingSample);

        // Fetch the PersonEntity
        PersonEntity personEntity = personRepo.findById(sampleReq.getPersonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

        // Find the existing PersonSampleEntity
        PersonSampleEntity personSampleEntity = personSampleRepo.findBySampleEntityAndPersonEntity(updatedSample, personEntity);

        if (personSampleEntity != null) {
            // Log before updating
            LOG.debug("Updating existing PersonSampleEntity: " + personSampleEntity);

            // Update existing PersonSampleEntity
            personSampleEntity.setSampleEntity(updatedSample);
            personSampleEntity = personSampleRepo.save(personSampleEntity); // Ensure changes are saved

            LOG.debug("Updated PersonSampleEntity: " + personSampleEntity);
        } else {
            // Log if no entry found
            LOG.warn("No existing PersonSampleEntity found for SampleEntity ID: " + updatedSample.getId() + " and PersonEntity ID: " + personEntity.getId());
        }

        // Log the activity
        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing Sample data", "Existing Sample data is modified successfully. ",  currentUser);

        return dtoUtil.prepRes(updatedSample);
    }

    @PreAuthorize("hasAuthority('SAMPLE_D')")
    @Override
    public void delete(Long id)  {
        LOG.info("----- Deleting Sample. -----");
        Optional<SampleEntity> optionalSampleEntity = sampleRepo.findById(id);
        optionalSampleEntity.orElseThrow(() -> new CustomException("COU001"));
        sampleRepo.delete(optionalSampleEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Sample", "Existing Sample data is deleted successfully. "  ,  currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Person Sample Data. ------");

        List<SampleReq> sampleReqs = extractDataFromFile(multipartFile.getInputStream());

        List<SampleEntity> sampleEntities = new ArrayList<>();
        List<PersonSampleEntity> personSampleEntities = new ArrayList<>();

        for (SampleReq sampleReq : sampleReqs) {
            SampleEntity sampleEntity = dtoUtil.reqToEntity(sampleReq);
            SampleEntity savedSample = sampleRepo.save(sampleEntity);
            sampleEntities.add(savedSample);

            PersonEntity personEntity = personRepo.findById(sampleReq.getPersonId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));

            PersonSampleEntity personSampleEntity = new PersonSampleEntity();
            personSampleEntity.setPersonEntity(personEntity);
            personSampleEntity.setSampleEntity(savedSample);
            personSampleEntities.add(personSampleEntity);
        }

        // Save all PersonSampleEntity instances
        personSampleRepo.saveAll(personSampleEntities);

        return !sampleEntities.isEmpty() && !personSampleEntities.isEmpty();
    }

    private List<SampleReq> extractDataFromFile(InputStream inputStream) throws IOException{
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<SampleReq> sampleReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            SampleReq sampleReq = extractDataFromRow(row);
            sampleReqs.add(sampleReq);
        }

        workbook.close();
        return sampleReqs;
    }

    private SampleReq extractDataFromRow(Row row) {
        SampleReq sampleReq = new SampleReq();

        if (row != null) {
            // Assuming the Sample data is in columns 0 and 1
            Long personId = DataValidationForExcelImport.getLongCellValueOrDefault(row, 0);
            String sampleName = DataValidationForExcelImport.getCellValueOrDefault(row, 1);

            sampleReq.setPersonId(personId);
            sampleReq.setType(sampleName);
        }

        return sampleReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "id";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        PersonSampleExcelExporter exportPersonSampleAllData = new PersonSampleExcelExporter();
        List<PersonSampleEntity> personSampleEntities = personSampleRepo.findAll( Sort.by(sortOrder, sortBy));
        List<PersonSampleRes> feedbackResDtos = personSampleEntities.stream().map(personSampleEntityDtoUtil::prepRes)
                .collect(Collectors.toList());
        return exportPersonSampleAllData.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<SampleRes> findSampleByName(String type) {
        return sampleRepo.findBySampleContaining(type)
                .stream()
                .map(sampleEntity -> {
                    SampleRes sampleRes = new SampleRes();
                    sampleRes.setId(sampleEntity.getId());
                    sampleRes.setType(sampleEntity.getType());

                    List<PersonSampleEntity> personSampleEntityList = personSampleRepo.findBySampleEntityId(sampleEntity.getId());
                    if (!personSampleEntityList.isEmpty()) {
                        PersonSampleEntity personSampleEntity = personSampleEntityList.get(0);
                        sampleRes.setPersonId(personSampleEntity.getPersonEntity().getId());
                    }

                    return sampleRes;
                })
                .collect(Collectors.toList());
    }


}

