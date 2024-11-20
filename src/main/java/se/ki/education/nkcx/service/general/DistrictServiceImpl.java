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
import se.ki.education.nkcx.dto.request.DistrictReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.DistrictRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.DistrictEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.DistrictRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.DistrictExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class DistrictServiceImpl implements DistrictService {

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private DistrictRepo districtRepo;

    private final DtoUtil<DistrictEntity, DistrictReq, DistrictRes> dtoUtil;

    private final PaginationDtoUtil<DistrictEntity, DistrictReq, DistrictRes> paginationDtoUtil;

    public DistrictServiceImpl(LogService logService, DistrictRepo districtRepo, DtoUtil<DistrictEntity, DistrictReq, DistrictRes> dtoUtil, PaginationDtoUtil<DistrictEntity, DistrictReq, DistrictRes> paginationDtoUtil) {
        this.logService = logService;
        this.districtRepo = districtRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('DISTRICT_C')")
    @Override
    public DistrictRes save(DistrictReq districtReq) {
        LOG.info("----- Saving District. -----");
        districtRepo.findByDistrict(districtReq.getDistrict()).ifPresent(districtEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.DISTRICT002.getMessage());
        });
        DistrictEntity entity = dtoUtil.reqToEntity(districtReq);
        DistrictEntity savedEntity = districtRepo.save(entity);
        DistrictRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new District", "A new District data is added successfully. ", currentUser);


        return res;
    }

    @Override
    public PaginationRes<DistrictRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting District. -----");

        List<String> validFields = Arrays.asList("district");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'district'", sortBy);
            sortBy = "district";
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<DistrictEntity> districtEntityPage = districtRepo.findAll(pageable);

        List<DistrictRes> districtRes = districtEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(districtEntityPage, districtRes);
    }

    @Override
    public DistrictRes update(DistrictReq districtReq) {
        LOG.info("----- Updating District. -----");
        Optional<DistrictEntity> optionalDistrictEntity = districtRepo.findById(districtReq.getId());
        optionalDistrictEntity.orElseThrow(() -> new CustomException("COU001"));
        DistrictEntity districtEntity = optionalDistrictEntity.get();
        dtoUtil.setUpdatedValue(districtReq, districtEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing District data", "Existing District data is modified successfully.", currentUser);

        return dtoUtil.prepRes(districtEntity);
    }

    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting District. -----");
        Optional<DistrictEntity> optionalDistrictEntity = districtRepo.findById(id);
        optionalDistrictEntity.orElseThrow(() -> new CustomException("COU001"));
        districtRepo.delete(optionalDistrictEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing District", "Existing District data is deleted successfully. ", currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing District Data. ------");

        List<DistrictReq> districtReqs = extractDataFromFile(multipartFile.getInputStream());

        List<DistrictEntity> districtEntities = new ArrayList<>();
        for (DistrictReq districtReq : districtReqs) {
            DistrictEntity districtEntity = dtoUtil.reqToEntity(districtReq);
            districtEntities.add(districtEntity);

        }
        return districtRepo.saveAll(districtEntities).size() > 0;
    }

    private List<DistrictReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<DistrictReq> districtReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            DistrictReq districtReq = extractDataFromRow(row);
            districtReqs.add(districtReq);
        }

        workbook.close();
        return districtReqs;
    }

    private DistrictReq extractDataFromRow(Row row) {
        DistrictReq districtReq = new DistrictReq();

        districtReq.setDistrict(DataValidationForExcelImport.getCellValueOrDefault(row, 0));
        districtReq.setDistrictName(DataValidationForExcelImport.getCellValueOrDefault(row, 1));

        return districtReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "district";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        DistrictExcelExporter districtExcelExporter = new DistrictExcelExporter();
        List<DistrictEntity> districtEntities = districtRepo.findAll(Sort.by(sortOrder, sortBy));
        List<DistrictRes> feedbackResDtos = districtEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return districtExcelExporter.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<DistrictRes> findDistrictByName(String district) {
        return districtRepo.findByDistrictContaining(district).stream()
                .map(districtEntity -> {
                    DistrictRes districtRes = new DistrictRes();
                    districtRes.setId(districtEntity.getId());
                    districtRes.setDistrict(districtEntity.getDistrict());
                    districtRes.setDistrictName(districtEntity.getDistrictName());

                    return districtRes;
                })
                .collect(Collectors.toList());
    }

}
