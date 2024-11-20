package se.ki.education.nkcx.service.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import se.ki.education.nkcx.dto.request.LaboratoryReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.LaboratoryEntity;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.LaboratoryRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.LaboratoryExcelExporter;
import se.ki.education.nkcx.util.excelExport.PersonExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class LaboratoryServiceImpl implements LaboratoryService {

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private LaboratoryRepo laboratoryRepo;

    private final DtoUtil<LaboratoryEntity, LaboratoryReq, LaboratoryRes> dtoUtil;

    private final PaginationDtoUtil<LaboratoryEntity, LaboratoryReq, LaboratoryRes> paginationDtoUtil;

    public LaboratoryServiceImpl(LogService logService, LaboratoryRepo laboratoryRepo, DtoUtil<LaboratoryEntity, LaboratoryReq, LaboratoryRes> dtoUtil, PaginationDtoUtil<LaboratoryEntity, LaboratoryReq, LaboratoryRes> paginationDtoUtil) {
        this.logService = logService;
        this.laboratoryRepo = laboratoryRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('LABORATORY_C')")
    @Override
    public LaboratoryRes save(LaboratoryReq laboratoryReq) {
        LOG.info("----- Saving Laboratory. -----");
        laboratoryRepo.findByName(laboratoryReq.getName()).ifPresent(laboratoryEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.LABORATORY002.getMessage());
        });
        LaboratoryEntity entity = dtoUtil.reqToEntity(laboratoryReq);
        LaboratoryEntity savedEntity =  laboratoryRepo.save(entity);
        LaboratoryRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new laboratory", "A new laboratory data is added successfully. ",  currentUser);


        return  res;
    }

    @Override
    public PaginationRes<LaboratoryRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting Laboratory. -----");

        List<String> validFields = Arrays.asList("name", "isInUse", "sosLab", "sosLabName", "sosLongName", "region");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'name'", sortBy);
            sortBy = "name";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<LaboratoryEntity> laboratoryEntityPage = laboratoryRepo.findAll(pageable);

        if ("isInUse".equals(sortBy)) {
            laboratoryEntityPage = laboratoryRepo.findByIsInUseTrue(pageable);
        } else {
            laboratoryEntityPage = laboratoryRepo.findAll(pageable);
        }
        List<LaboratoryRes> laboratoryRes = laboratoryEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(laboratoryEntityPage, laboratoryRes);
    }

    @Override
    public LaboratoryRes update(LaboratoryReq laboratoryReq) {
        LOG.info("----- Updating Laboratory. -----");
        Optional<LaboratoryEntity> optionalLaboratoryEntity = laboratoryRepo.findById(laboratoryReq.getId());
        optionalLaboratoryEntity.orElseThrow(() -> new CustomException("COU001"));
        LaboratoryEntity laboratoryEntity = optionalLaboratoryEntity.get();
        dtoUtil.setUpdatedValue(laboratoryReq, laboratoryEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing laboratory data", "Existing laboratory data is modified successfully.", currentUser);

        return dtoUtil.prepRes(laboratoryEntity);
    }

    @Override
    public void delete(Long id) {

        LOG.info("----- Deleting Laboratory. -----");
        Optional<LaboratoryEntity> optionalLaboratoryEntity = laboratoryRepo.findById(id);
        optionalLaboratoryEntity.orElseThrow(() -> new CustomException("COU001"));
        laboratoryRepo.delete(optionalLaboratoryEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing laboratory", "Existing laboratory data is deleted successfully. " ,  currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Laboratory Data. ------");

        List<LaboratoryReq> laboratoryReqs = extractDataFromFile(multipartFile.getInputStream());

        List<LaboratoryEntity> laboratoryEntities = new ArrayList<>();
        for (LaboratoryReq laboratoryReq : laboratoryReqs) {
            LaboratoryEntity laboratoryEntity = dtoUtil.reqToEntity(laboratoryReq);
            laboratoryEntities.add(laboratoryEntity);

        }
        return laboratoryRepo.saveAll(laboratoryEntities).size() > 0;
    }

    private List<LaboratoryReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<LaboratoryReq> laboratoryReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            LaboratoryReq laboratoryReq = extractDataFromRow(row);
            laboratoryReqs.add(laboratoryReq);
        }

        workbook.close();
        return laboratoryReqs;
    }

    private LaboratoryReq extractDataFromRow(Row row) {
        LaboratoryReq laboratoryReq = new LaboratoryReq();



        // Extract other fields
        laboratoryReq.setName(DataValidationForExcelImport.getCellValueOrDefault(row, 0));
        laboratoryReq.setIsInUse(Boolean.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 1)));
        laboratoryReq.setSosLab(Integer.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 2)));
        laboratoryReq.setSosLabName(DataValidationForExcelImport.getCellValueOrDefault(row, 3));
        laboratoryReq.setSosLongName(DataValidationForExcelImport.getCellValueOrDefault(row, 4));
        laboratoryReq.setRegion(DataValidationForExcelImport.getCellValueOrDefault(row, 5));



        return laboratoryReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "name";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        LaboratoryExcelExporter laboratoryExcelExporter = new LaboratoryExcelExporter();
        List<LaboratoryEntity> laboratoryEntities = laboratoryRepo.findAll( Sort.by(sortOrder, sortBy));
        List<LaboratoryRes> feedbackResDtos = laboratoryEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return laboratoryExcelExporter.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<LaboratoryRes> findLaboratoryByName(String name) {
        return laboratoryRepo.findByLaboratoryContaining(name).stream()
                .map(laboratoryEntity -> {
                    LaboratoryRes laboratoryRes = new LaboratoryRes();
                    laboratoryRes.setId(laboratoryEntity.getId());
                    laboratoryRes.setName(laboratoryEntity.getName());
                    laboratoryRes.setIsInUse(laboratoryEntity.getIsInUse());
                    laboratoryRes.setSosLab(laboratoryEntity.getSosLab());
                    laboratoryRes.setSosLabName(laboratoryEntity.getSosLabName());
                    laboratoryRes.setSosLongName(laboratoryEntity.getSosLongName());
                    laboratoryRes.setRegion(laboratoryEntity.getRegion());

                    return laboratoryRes;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LaboratoryRes> filterIsInUse(Boolean isInUse) {
        LOG.info("Filtering by isInUse = {}", isInUse);

        // Retrieve the list of persons based on isValidPNR
        List<LaboratoryEntity> laboratoryEntities = laboratoryRepo.findByIsInUse(isInUse);

        // Convert entities to response objects
        return laboratoryEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }
}
