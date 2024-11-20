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
import se.ki.education.nkcx.dto.request.CountyReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CountyRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.CountyEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.CountyRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.CountyExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountyServiceImpl implements CountyService {

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private CountyRepo countyRepo;

    private final DtoUtil<CountyEntity, CountyReq, CountyRes> dtoUtil;

    private final PaginationDtoUtil<CountyEntity, CountyReq, CountyRes> paginationDtoUtil;

    public CountyServiceImpl(LogService logService, CountyRepo countyRepo, DtoUtil<CountyEntity, CountyReq, CountyRes> dtoUtil, PaginationDtoUtil<CountyEntity, CountyReq, CountyRes> paginationDtoUtil) {
        this.logService = logService;
        this.countyRepo = countyRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('COUNTY_C')")
    @Override
    public CountyRes save(CountyReq countyReq) {
        LOG.info("----- Saving County. -----");
        countyRepo.findByName(countyReq.getName()).ifPresent(countyEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.COUNTY002.getMessage());
        });
        CountyEntity entity = dtoUtil.reqToEntity(countyReq);
        CountyEntity savedEntity =  countyRepo.save(entity);
        CountyRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new County", "A new County data is added successfully. ",  currentUser);

        return  res;
    }

    @Override
    public PaginationRes<CountyRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting County. -----");

        List<String> validFields = Arrays.asList("name");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'name'", sortBy);
            sortBy = "name";
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<CountyEntity> countyEntityPage = countyRepo.findAll(pageable);

        List<CountyRes> countyRes = countyEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(countyEntityPage, countyRes);
    }

    @Override
    public CountyRes update(CountyReq countyReq) {
        LOG.info("----- Updating County. -----");
        Optional<CountyEntity> optionalCountyEntity = countyRepo.findById(countyReq.getId());
        optionalCountyEntity.orElseThrow(() -> new CustomException("COU001"));
        CountyEntity countyEntity = optionalCountyEntity.get();
        dtoUtil.setUpdatedValue(countyReq, countyEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing county data", "Existing county data is modified successfully.", currentUser);

        return dtoUtil.prepRes(countyEntity);
    }

    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting County. -----");
        Optional<CountyEntity> optionalCountyEntity = countyRepo.findById(id);
        optionalCountyEntity.orElseThrow(() -> new CustomException("COU001"));
        countyRepo.delete(optionalCountyEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing County", "Existing County data is deleted successfully. " ,  currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing County Data. ------");

        List<CountyReq> countyReqs = extractDataFromFile(multipartFile.getInputStream());

        List<CountyEntity> countyEntities = new ArrayList<>();
        for (CountyReq countyReq : countyReqs) {
            CountyEntity countyEntity = dtoUtil.reqToEntity(countyReq);
            countyEntities.add(countyEntity);

        }
        return countyRepo.saveAll(countyEntities).size() > 0;
    }

    private List<CountyReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<CountyReq> countyReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            CountyReq countyReq = extractDataFromRow(row);
            countyReqs.add(countyReq);
        }

        workbook.close();
        return countyReqs;
    }

    private CountyReq extractDataFromRow(Row row) {
        CountyReq countyReq = new CountyReq();

        countyReq.setName(DataValidationForExcelImport.getCellValueOrDefault(row, 0));

        return countyReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "name";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        CountyExcelExporter countyExcelExporter = new CountyExcelExporter();
        List<CountyEntity> countyEntities = countyRepo.findAll( Sort.by(sortOrder, sortBy));
        List<CountyRes> feedbackResDtos = countyEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return countyExcelExporter.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<CountyRes> findCountyByName(String name) {
        return countyRepo.findByCountyContaining(name).stream()
                .map(countyEntity -> {
                    CountyRes countyRes = new CountyRes();
                    countyRes.setId(countyEntity.getId());
                    countyRes.setName(countyEntity.getName());

                    return countyRes;
                })
                .collect(Collectors.toList());
    }
}
