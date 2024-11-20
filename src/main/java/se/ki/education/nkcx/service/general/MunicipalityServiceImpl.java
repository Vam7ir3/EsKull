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
import se.ki.education.nkcx.dto.request.MunicipalityReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.MunicipalityEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.MunicipalityRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.MunicipilityExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class MunicipalityServiceImpl implements MunicipalityService {

    private static final Logger LOG = LogManager.getLogger();

    private final MunicipalityRepo municipalityRepo;

    private final LogService logService;

    private final DtoUtil<MunicipalityEntity, MunicipalityReq, MunicipalityRes> dtoUtil;

    private final PaginationDtoUtil<MunicipalityEntity, MunicipalityReq, MunicipalityRes> paginationDtoUtil;

    public MunicipalityServiceImpl(MunicipalityRepo municipalityRepo, LogService logService, DtoUtil<MunicipalityEntity, MunicipalityReq, MunicipalityRes> dtoUtil, PaginationDtoUtil<MunicipalityEntity, MunicipalityReq, MunicipalityRes> paginationDtoUtil) {
        this.municipalityRepo = municipalityRepo;
        this.logService = logService;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('MUNICIPALITY_C')")
    @Override
    public MunicipalityRes save(MunicipalityReq municipalityReq) {
        LOG.info("----- Saving Municipality. -----");
        municipalityRepo.findByName(municipalityReq.getName()).ifPresent(municipalityEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.MUNICIPALITY002.getMessage());
        });
        MunicipalityEntity entity = dtoUtil.reqToEntity(municipalityReq);
        MunicipalityEntity savedEntity = municipalityRepo.save(entity);
        MunicipalityRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Municipality", "A new Municipality data is added successfully. ", currentUser);


        return res;
    }

    @Override
    public PaginationRes<MunicipalityRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting Municipality. -----");

        List<String> validFields = Arrays.asList("name");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'name'", sortBy);
            sortBy = "name";
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<MunicipalityEntity> municipalityEntityPage = municipalityRepo.findAll(pageable);

        List<MunicipalityRes> municipalityRes = municipalityEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(municipalityEntityPage, municipalityRes);
    }

    @PreAuthorize("hasAuthority('MUNICIPALITY_U')")
    @Override
    public MunicipalityRes update(MunicipalityReq municipalityReq) {
        LOG.info("----- Updating Municipality. -----");
        Optional<MunicipalityEntity> optionalMunicipalityEntity = municipalityRepo.findById(municipalityReq.getId());
        optionalMunicipalityEntity.orElseThrow(() -> new CustomException("COU001"));
        MunicipalityEntity municipalityEntity = optionalMunicipalityEntity.get();
        dtoUtil.setUpdatedValue(municipalityReq, municipalityEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing municipality data", "Existing municipality data is modified successfully.", currentUser);

        return dtoUtil.prepRes(municipalityEntity);
    }

    @PreAuthorize("hasAuthority('MUNICIPALITY_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Municipality. -----");
        Optional<MunicipalityEntity> optionalMunicipalityEntity = municipalityRepo.findById(id);
        optionalMunicipalityEntity.orElseThrow(() -> new CustomException("COU001"));
        municipalityRepo.delete(optionalMunicipalityEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Municipality", "Existing municipality data is deleted successfully. ", currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Municipality Data. ------");

        List<MunicipalityReq> municipalityReqs = extractDataFromFile(multipartFile.getInputStream());

        List<MunicipalityEntity> municipalityEntities = new ArrayList<>();
        for (MunicipalityReq municipalityReq : municipalityReqs) {
            MunicipalityEntity municipalityEntity = dtoUtil.reqToEntity(municipalityReq);
            municipalityEntities.add(municipalityEntity);

        }
        return municipalityRepo.saveAll(municipalityEntities).size() > 0;
    }

    private List<MunicipalityReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<MunicipalityReq> municipalityReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            MunicipalityReq municipalityReq = extractDataFromRow(row);
            municipalityReqs.add(municipalityReq);
        }

        workbook.close();
        return municipalityReqs;
    }

    private MunicipalityReq extractDataFromRow(Row row) {
        MunicipalityReq municipalityReq = new MunicipalityReq();

        municipalityReq.setName(DataValidationForExcelImport.getCellValueOrDefault(row, 1));
//        municipalityReq.setYear(Integer.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 1)));
        String yearCellValue = DataValidationForExcelImport.getCellValueOrDefault(row, 2);
        try {
            municipalityReq.setYear(Integer.parseInt(yearCellValue.trim()));
        } catch (NumberFormatException e) {
            LOG.warn("Invalid year format for value: '{}'. Skipping this row.", yearCellValue);
            return null;
        }

        return municipalityReq;
    }


    @Override
    public Workbook exportFile() {
        String sortBy = "id";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        MunicipilityExcelExporter municipilityExcelExporter = new MunicipilityExcelExporter();
        List<MunicipalityEntity> municipalityEntities = municipalityRepo.findAll(Sort.by(sortOrder, sortBy));
        List<MunicipalityRes> feedbackResDtos = municipalityEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return municipilityExcelExporter.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<MunicipalityRes> findMunicipalityByName(String name) {
        return municipalityRepo.findByName(name)
                .stream()
                .map(municipalityEntity -> {
                    MunicipalityRes municipalityRes = new MunicipalityRes();
                    municipalityRes.setId(municipalityEntity.getId());
                    municipalityRes.setName(municipalityEntity.getName());
                    municipalityRes.setYear(municipalityEntity.getYear());
                    return municipalityRes;
                })
                .collect(Collectors.toList());
    }
}
