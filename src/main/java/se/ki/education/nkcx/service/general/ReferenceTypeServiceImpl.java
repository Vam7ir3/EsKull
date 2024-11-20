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
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.ReferenceTypeReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.ReferenceTypeRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.ReferenceTypeEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.ReferenceTypeRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.ReferenceTypeExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ReferenceTypeServiceImpl implements ReferenceTypeService {

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private ReferenceTypeRepo referenceTypeRepo;

    private final DtoUtil<ReferenceTypeEntity, ReferenceTypeReq, ReferenceTypeRes> dtoUtil;

    private final PaginationDtoUtil<ReferenceTypeEntity, ReferenceTypeReq, ReferenceTypeRes> paginationDtoUtil;

    public ReferenceTypeServiceImpl(LogService logService, ReferenceTypeRepo referenceTypeRepo, DtoUtil<ReferenceTypeEntity, ReferenceTypeReq, ReferenceTypeRes> dtoUtil, PaginationDtoUtil<ReferenceTypeEntity, ReferenceTypeReq, ReferenceTypeRes> paginationDtoUtil) {
        this.logService = logService;
        this.referenceTypeRepo = referenceTypeRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }


    @PreAuthorize("hasAuthority('REFERENCETYPE_C')")
    @Override
    public ReferenceTypeRes save(ReferenceTypeReq referenceTypeReq) {
        LOG.info("----- Saving ReferenceType. -----");
        referenceTypeRepo.findByType(referenceTypeReq.getType()).ifPresent(referenceTypeEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.INVITATIONTYPE002.getMessage());
        });
        ReferenceTypeEntity entity = dtoUtil.reqToEntity(referenceTypeReq);
        ReferenceTypeEntity savedEntity = referenceTypeRepo.save(entity);
        ReferenceTypeRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new ReferenceTypeEntity", "A new ReferenceTypeEntity data is added successfully. ", currentUser);

        return res;
    }

    @Override
    public PaginationRes<ReferenceTypeRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting ReferenceType. -----");

        List<String> validFields = Arrays.asList("type");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'type'", sortBy);
            sortBy = "type";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<ReferenceTypeEntity> referenceTypeEntityPage = referenceTypeRepo.findAll(pageable);

        List<ReferenceTypeRes> referenceTypeRes = referenceTypeEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(referenceTypeEntityPage, referenceTypeRes);
    }

    @Override
    public ReferenceTypeRes update(ReferenceTypeReq referenceTypeReq) {
        LOG.info("----- Updating ReferenceType. -----");
        Optional<ReferenceTypeEntity> optionalReferenceTypeEntity = referenceTypeRepo.findById(referenceTypeReq.getId());
        optionalReferenceTypeEntity.orElseThrow(() -> new CustomException("COU001"));
        ReferenceTypeEntity referenceTypeEntity = optionalReferenceTypeEntity.get();
        dtoUtil.setUpdatedValue(referenceTypeReq, referenceTypeEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing ReferenceType data", "Existing ReferenceType data is modified successfully.", currentUser);

        return dtoUtil.prepRes(referenceTypeEntity);
    }

    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting ReferenceType. -----");
        Optional<ReferenceTypeEntity> optionalReferenceTypeEntity = referenceTypeRepo.findById(id);
        optionalReferenceTypeEntity.orElseThrow(() -> new CustomException("COU001"));
        referenceTypeRepo.delete(optionalReferenceTypeEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing ReferenceType", "Existing ReferenceType data is deleted successfully. ", currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing ReferenceType Data. ------");

        List<ReferenceTypeReq> referenceTypeReqs = extractDataFromFile(multipartFile.getInputStream());

        List<ReferenceTypeEntity> referenceTypeEntities = new ArrayList<>();
        for (ReferenceTypeReq referenceTypeReq : referenceTypeReqs) {
            ReferenceTypeEntity referenceTypeEntity = dtoUtil.reqToEntity(referenceTypeReq);
            referenceTypeEntities.add(referenceTypeEntity);

        }
        return referenceTypeRepo.saveAll(referenceTypeEntities).size() > 0;
    }

    private List<ReferenceTypeReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<ReferenceTypeReq> referenceTypeReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            ReferenceTypeReq referenceTypeReq = extractDataFromRow(row);
            referenceTypeReqs.add(referenceTypeReq);
        }

        workbook.close();
        return referenceTypeReqs;
    }

    private ReferenceTypeReq extractDataFromRow(Row row) {
        ReferenceTypeReq referenceTypeReq = new ReferenceTypeReq();

        referenceTypeReq.setType(DataValidationForExcelImport.getCellValueOrDefault(row, 0));

        return referenceTypeReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "type";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        ReferenceTypeExcelExporter referenceTypeExcelExporter = new ReferenceTypeExcelExporter();
        List<ReferenceTypeEntity> referenceTypeEntities = referenceTypeRepo.findAll(Sort.by(sortOrder, sortBy));
        List<ReferenceTypeRes> feedbackResDtos = referenceTypeEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return referenceTypeExcelExporter.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<ReferenceTypeRes> findReferenceTypeByName(String type) {
        return referenceTypeRepo.findByReferenceContaining(type).stream()
                .map(referenceTypeEntity -> {
                    ReferenceTypeRes referenceTypeRes = new ReferenceTypeRes();
                    referenceTypeRes.setId(referenceTypeEntity.getId());
                    referenceTypeRes.setType(referenceTypeEntity.getType());


                    return referenceTypeRes;
                })
                .collect(Collectors.toList());
    }
}
