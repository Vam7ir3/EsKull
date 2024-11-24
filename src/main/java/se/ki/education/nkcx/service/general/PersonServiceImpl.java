package se.ki.education.nkcx.service.general;

import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.*;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.PersonRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.PersonExcelExporter;


import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PersonServiceImpl implements  PersonService{

    private static final Logger LOG = LogManager.getLogger();

    private final PersonRepo personRepo;

    private LogService logService;

    private EntityManager entityManager;

    private final DtoUtil<PersonEntity, PersonReq, PersonRes> dtoUtil;

    private final PaginationDtoUtil<PersonEntity, PersonReq, PersonRes> paginationDtoUtil;


    @Autowired
    public PersonServiceImpl(PersonRepo personRepo, LogService logService, EntityManager entityManager, DtoUtil<PersonEntity, PersonReq, PersonRes> dtoUtil, PaginationDtoUtil<PersonEntity, PersonReq, PersonRes> paginationDtoUtil) {
        this.personRepo = personRepo;
        this.logService = logService;
        this.entityManager = entityManager;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('PERSON_C')")
    @Override
    public PersonRes save(PersonReq personReq) {
        LOG.info("----- Saving Person. -----");
        personRepo.findByPnr(personReq.getPnr()).ifPresent(personEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.PERSON002.getMessage());
        });
        PersonEntity entity = dtoUtil.reqToEntity(personReq);
        PersonEntity savedEntity =  personRepo.save(entity);
        PersonRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new person", "A new person data is added successfully. ",  currentUser);


        return  res;
//        return dtoUtil.prepRes(personRepo.save(dtoUtil.reqToEntity(personReq)));
    }


    @Override
    public PaginationRes<PersonRes> get(PaginationReq paginationReq) {

        LOG.info("----- Getting Person. -----");

        List<String> validFields = Arrays.asList("isValidPNR");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'pnr'", sortBy);
            sortBy = "pnr";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<PersonEntity> personEntityPage = personRepo.findAll(pageable);

        if ("isValidPNR".equals(sortBy)) {
            personEntityPage = personRepo.findByIsValidPNRTrue(pageable);
        } else {
            personEntityPage = personRepo.findAll(pageable);
        }
        List<PersonRes> personRes = personEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(personEntityPage, personRes);


//        List<String> fields = Arrays.asList("email");
//        String sortBy = "address";//Default sortBy
//        Sort.Direction sortOrder = Sort.Direction.ASC;
//        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, personRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('PERSON_U')")
    @Override
    public PersonRes update(PersonReq personReq) {
        LOG.info("----- Updating Person. -----");
        Optional<PersonEntity> optionalPersonEntity = personRepo.findById(personReq.getId());
        optionalPersonEntity.orElseThrow(() -> new CustomException("COU001"));
        PersonEntity personEntity = optionalPersonEntity.get();
        dtoUtil.setUpdatedValue(personReq, personEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing person data", "Existing person data is modified successfully.", currentUser);

        return dtoUtil.prepRes(personEntity);
    }

    @PreAuthorize("hasAuthority('PERSON_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Person. -----");
        Optional<PersonEntity> optionalPersonEntity = personRepo.findById(id);
        optionalPersonEntity.orElseThrow(() -> new CustomException("COU001"));
        personRepo.delete(optionalPersonEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing person", "Existing person data is deleted successfully. ",  currentUser);
    }

    public PaginationRes<PersonRes> search(
            PaginationReq paginationReq,
            List<Integer> pnr,
            List<Boolean> isValidPNR) {

        LOG.info("----- Searching Person with multiple criteria -----");

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(),
                Sort.by(paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC,
                        paginationReq.getSortBy()));

        Page<PersonEntity> personEntityPage = personRepo.findByPnrInAndIsValidPNRIn(
                pnr, isValidPNR, pageable);

        List<PersonRes> personResList = personEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(personEntityPage, personResList);
    }



    public Boolean importData(MultipartFile multipartFile) throws IOException{

        LOG.info("---- Importing Person Data. ------");

        List<PersonReq> personReqs = extractDataFromFile(multipartFile.getInputStream());

        List<PersonEntity> personEntities = new ArrayList<>();
        for (PersonReq personReq : personReqs) {
            PersonEntity personEntity = dtoUtil.reqToEntity(personReq);
            personEntities.add(personEntity);

        }
        return personRepo.saveAll(personEntities).size() > 0;
    }

//    private List<PersonReq> extractDataFromFile(InputStream inputStream) throws IOException {
//        Workbook workbook = WorkbookFactory.create(inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//
//        List<PersonReq> personReqs = new ArrayList<>();
//
//        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//            Row row = sheet.getRow(i);
//            PersonReq personReq = extractDataFromRow(row);
//            personReqs.add(personReq);
//        }
//
//        workbook.close();
//        return personReqs;
//    }

    private List<PersonReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<PersonReq> personReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                LOG.info("Skipping empty row at index: {}", i);
                continue;
            }
            try {
                PersonReq personReq = extractDataFromRow(row);
                personReqs.add(personReq);
            } catch (Exception e) {
                LOG.warn("Error processing row at index: {}. Error: {}", i, e.getMessage());
                // Optionally, you can choose to throw an exception here if you want to halt the import process
                // throw new CustomException("Error processing row " + i + ": " + e.getMessage());
            }
        }

        workbook.close();
        return personReqs;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }



    private PersonReq extractDataFromRow(Row row) {
        PersonReq personReq = new PersonReq();
        LocalDate dateOfBirth = null;
        boolean isByYear =  false;

        String birthDateString = DataValidationForExcelImport.getCellValueOrDefault(row, 2); // birth_date column
        if (!birthDateString.isEmpty() && !birthDateString.equalsIgnoreCase("No data")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dateOfBirth = LocalDate.parse(birthDateString, formatter);
                LOG.debug("Successfully parsed birth_date: {}", birthDateString);

                isByYear = false;

            } catch (DateTimeParseException e) {
                LOG.warn("Could not parse birth_date: {}. Will try birth_yr.", birthDateString);
            }
        }

        // Only if birth_date is null, try to use birth year
        if (dateOfBirth == null) {
            String birthYearString = DataValidationForExcelImport.getCellValueOrDefault(row, 4); // birth_yr column
            if (!birthYearString.isEmpty() && !birthYearString.equalsIgnoreCase("No data")) {
                try {
                    int birthYear = Integer.parseInt(birthYearString.trim());
                    dateOfBirth = LocalDate.of(birthYear, 1, 1); // Set to January 1st of the birth year

                    isByYear = true;

                    LOG.debug("Using birth year: {}. Set date to: {}", birthYear, dateOfBirth);
                } catch (NumberFormatException e) {
                    LOG.warn("Invalid birth year format: {}", birthYearString);
                } catch (DateTimeException e) {
                    LOG.warn("Invalid year value: {}", birthYearString);
                }
            }
        }

        // Set the date of birth in the PersonReq object
        if (dateOfBirth != null) {
            personReq.setDateOfBirth(dateOfBirth.toString());
        } else {
            LOG.warn("No valid birth date or year found for row {}", row.getRowNum());
        }

        personReq.setIsByYear(isByYear);

        // Extract other fields
        try {
            String pnrString = DataValidationForExcelImport.getCellValueOrDefault(row, 1);
            if (!pnrString.isEmpty() && !pnrString.equalsIgnoreCase("No data")) {
                personReq.setPnr(Integer.valueOf(pnrString.trim()));
            }
        } catch (NumberFormatException e) {
            LOG.warn("Invalid PNR format in row {}", row.getRowNum());
        }

        String isValidPNRString = DataValidationForExcelImport.getCellValueOrDefault(row, 3);
        if (!isValidPNRString.isEmpty() && !isValidPNRString.equalsIgnoreCase("No data")) {
            personReq.setIsValidPNR(Boolean.valueOf(isValidPNRString.trim()));
        }

        return personReq;
    }

    public Workbook exportFile() {
        String sortBy = "pnr";
        Sort.Direction sortOrder = Sort.Direction.ASC;

        PersonExcelExporter exportPersonAllData = new PersonExcelExporter();
        List<PersonEntity> personEntities = personRepo.findAll( Sort.by(sortOrder, sortBy));
        List<PersonRes> feedbackResDtos = personEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return exportPersonAllData.exportToExcel(feedbackResDtos);
    }

//    @Override
//    public List<PersonRes> filterPersonByIsValidPnr(Boolean isValidPNR) {
//        LOG.info("Filtering persons by isValidPNR = {}", isValidPNR);
//
//        // Retrieve the list of persons based on isValidPNR
//        List<PersonEntity> personEntities = personRepo.findByIsValidPNR(isValidPNR);
//
//        // Convert entities to response objects
//        return personEntities.stream()
//                .map(dtoUtil::prepRes)
//                .collect(Collectors.toList());
//    }

    @PreAuthorize("hasAuthority('PERSON_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationRes<PersonRes> filterPerson(PaginationReq paginationReq, @RequestParam(required = false) Boolean isValidPNR) {
        LOG.info("Filtering persons by isValidPNR = {}", isValidPNR);



        // Validate the sortBy field to ensure it is valid
        String sortBy = paginationReq.getSortBy();
        List<String> validFields = Arrays.asList("isValidPNR");

        if (sortBy == null || !validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'isValidPNR'", sortBy);
            sortBy = "isValidPNR"; // Default sortBy if invalid or null
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));

        Page<PersonEntity> personEntityPage = personRepo.findByIsValidPNR(isValidPNR, pageable);

        LOG.info("Found {} persons with isValidPNR = {}", personEntityPage.getTotalElements(), isValidPNR);

        List<PersonRes> personResList = personEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(personEntityPage, personResList);
    }
}
