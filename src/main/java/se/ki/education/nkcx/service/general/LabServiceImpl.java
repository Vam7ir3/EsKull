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
import se.ki.education.nkcx.dto.request.LabReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.LabRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.LabEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.LabRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.LabExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class LabServiceImpl implements LabService {

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private LabRepo labRepo;

    private final DtoUtil<LabEntity, LabReq, LabRes> dtoUtil;

    private final PaginationDtoUtil<LabEntity, LabReq, LabRes> paginationDtoUtil;

    public LabServiceImpl(LogService logService, LabRepo labRepo, DtoUtil<LabEntity, LabReq, LabRes> dtoUtil, PaginationDtoUtil<LabEntity, LabReq, LabRes> paginationDtoUtil) {
        this.logService = logService;
        this.labRepo = labRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('LAB_C')")
    @Override
    public LabRes save(LabReq labReq) {
        LOG.info("----- Saving Lab. -----");
        labRepo.findByName(labReq.getName()).ifPresent(labEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.LAB002.getMessage());
        });
        LabEntity entity = dtoUtil.reqToEntity(labReq);
        LabEntity savedEntity = labRepo.save(entity);
        LabRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Lab", "A new Lab data is added successfully. ", currentUser);


        return res;
    }

    @Override
    public PaginationRes<LabRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting Lab. -----");

        List<String> validFields = Arrays.asList("name");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'name'", sortBy);
            sortBy = "name";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<LabEntity> labEntityPage = labRepo.findAll(pageable);

        List<LabRes> labRes = labEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(labEntityPage, labRes);
    }

    @Override
    public LabRes update(LabReq labReq) {
        LOG.info("----- Updating Lab. -----");
        Optional<LabEntity> optionalLabEntity = labRepo.findById(labReq.getId());
        optionalLabEntity.orElseThrow(() -> new CustomException("COU001"));
        LabEntity labEntity = optionalLabEntity.get();
        dtoUtil.setUpdatedValue(labReq, labEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing Lab data", "Existing Lab data is modified successfully.", currentUser);

        return dtoUtil.prepRes(labEntity);
    }

    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Lab. -----");
        Optional<LabEntity> optionalLabEntity = labRepo.findById(id);
        optionalLabEntity.orElseThrow(() -> new CustomException("COU001"));
        labRepo.delete(optionalLabEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Lab", "Existing Lab data is deleted successfully. ", currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Lab Data. ------");

        List<LabReq> labReqs = extractDataFromFile(multipartFile.getInputStream());

        List<LabEntity> labEntities = new ArrayList<>();
        for (LabReq labReq : labReqs) {
            LabEntity labEntity = dtoUtil.reqToEntity(labReq);
            labEntities.add(labEntity);

        }
        return labRepo.saveAll(labEntities).size() > 0;
    }

    private List<LabReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<LabReq> labReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            LabReq labReq = extractDataFromRow(row);
            labReqs.add(labReq);
        }

        workbook.close();
        return labReqs;
    }

    private LabReq extractDataFromRow(Row row) {
        LabReq labReq = new LabReq();

        // Extract other fields
        labReq.setName(DataValidationForExcelImport.getCellValueOrDefault(row, 0));
        labReq.setIsInUse(Boolean.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 1)));

        return labReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "name";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        LabExcelExporter labExcelExporter = new LabExcelExporter();
        List<LabEntity> labEntities = labRepo.findAll( Sort.by(sortOrder, sortBy));
        List<LabRes> feedbackResDtos = labEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return labExcelExporter.exportToExcel(feedbackResDtos);
    }
}
