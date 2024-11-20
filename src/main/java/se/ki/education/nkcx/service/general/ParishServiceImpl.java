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
import se.ki.education.nkcx.dto.request.ParishReq;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.*;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.CountryRepo;
import se.ki.education.nkcx.repo.MunicipalityRepo;
import se.ki.education.nkcx.repo.ParishRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.ParishExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ParishServiceImpl implements ParishService {

    private static final Logger LOG = LogManager.getLogger();

    private final ParishRepo parishRepo;

    private final MunicipalityRepo municipalityRepo;

    private final CountryRepo countryRepo;

    private LogService logService;

    private final DtoUtil<ParishEntity, ParishReq, ParishRes> dtoUtil;

    private final PaginationDtoUtil<ParishEntity, ParishReq, ParishRes> paginationDtoUtil;

    public ParishServiceImpl(ParishRepo parishRepo, MunicipalityRepo municipalityRepo, CountryRepo countryRepo, LogService logService, DtoUtil<ParishEntity, ParishReq, ParishRes> dtoUtil, PaginationDtoUtil<ParishEntity, ParishReq, ParishRes> paginationDtoUtil) {
        this.parishRepo = parishRepo;
        this.municipalityRepo = municipalityRepo;
        this.countryRepo = countryRepo;
        this.logService = logService;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }


    @PreAuthorize("hasAuthority('PARISH_C')")
    @Override
    public ParishRes save(ParishReq parishReq) {
        LOG.info("----- Saving Parish. -----");
        parishRepo.findByName(parishReq.getName()).ifPresent(parishEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.PARISH002.getMessage());
        });
        ParishEntity entity = dtoUtil.reqToEntity(parishReq);
        ParishEntity savedEntity = parishRepo.save(entity);
        ParishRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Parish", "A new Parish data is added successfully. ", currentUser);

        return res;
    }

    @Override
    public PaginationRes<ParishRes> get(PaginationReq paginationReq) {

        LOG.info("----- Getting Parish. -----");

        List<String> validFields = Arrays.asList("name");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'name'", sortBy);
            sortBy = "name";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<ParishEntity> parishEntities = parishRepo.findAll(pageable);

        if ("name".equals(sortBy)) {
            parishEntities = parishRepo.findAll(pageable);
        } else {
            parishEntities = parishRepo.findAll(pageable);
        }
        List<ParishRes> parishRes = parishEntities.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(parishEntities, parishRes);
    }

    @Override
    public ParishRes update(ParishReq parishReq) {
        LOG.info("----- Updating Parish. -----");
        Optional<ParishEntity> optionalParishEntity = parishRepo.findById(parishReq.getId());
        optionalParishEntity.orElseThrow(() -> new CustomException("COU001"));
        ParishEntity parishEntity = optionalParishEntity.get();
        dtoUtil.setUpdatedValue(parishReq, parishEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing parish data", "Existing parish data is modified successfully.", currentUser);

        return dtoUtil.prepRes(parishEntity);
    }

    @PreAuthorize("hasAuthority('PARISH_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Parish. -----");
        Optional<ParishEntity> optionalParishEntity = parishRepo.findById(id);
        optionalParishEntity.orElseThrow(() -> new CustomException("COU001"));
        parishRepo.delete(optionalParishEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Parish", "Existing Parish data is deleted successfully. ", currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Parish Data. ------");

        List<ParishReq> parishReqs = extractDataFromFile(multipartFile.getInputStream());

        List<ParishEntity> parishEntities = new ArrayList<>();
        for (ParishReq parishReq : parishReqs) {
            ParishEntity parishEntity = dtoUtil.reqToEntity(parishReq);
            parishEntities.add(parishEntity);

        }
        return parishRepo.saveAll(parishEntities).size() > 0;
    }

    private List<ParishReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<ParishReq> parishReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            ParishReq parishReq = extractDataFromRow(row);
            parishReqs.add(parishReq);
        }

        workbook.close();
        return parishReqs;
    }

    private ParishReq extractDataFromRow(Row row) {
        ParishReq parishReq = new ParishReq();

        if (row != null) {

            String name = DataValidationForExcelImport.getCellValueOrDefault(row, 1);
            String registerDate = DataValidationForExcelImport.getCellValueOrDefault(row, 2);
            String dividedOtherCounty = DataValidationForExcelImport.getCellValueOrDefault(row, 3);
            Long municipalityId = Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 4));
            Long countyId = Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 5));

            String epochTime = convertDateToEpoch(registerDate);

            parishReq.setName(name);
            parishReq.setRegisterDate(epochTime);
            parishReq.setDividedOtherCounty(dividedOtherCounty);
            parishReq.setMunicipalityId(municipalityId);
            parishReq.setCountyId(countyId);
        }

        return parishReq;
    }

    private String convertDateToEpoch(String date) {
        if(date == null || date.trim().isEmpty()) {
            return date;
        }

        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate dateStr = LocalDate.parse(date.trim(), formatter);
                long epoch = dateStr.atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli();
                return String.valueOf(epoch);
            } catch (DateTimeParseException e) {
            }
        }
        return date;

    }


    @Override
    public Workbook exportFile() {
        String sortBy = "id";
        Sort.Direction sortOrder = Sort.Direction.ASC;

        ParishExcelExporter exportParishAllData = new ParishExcelExporter();
        List<ParishEntity> parishEntities = parishRepo.findAll(Sort.by(sortOrder, sortBy));
        List<ParishRes> feedbackResDtos = parishEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return exportParishAllData.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<ParishRes> findParishByName(String name) {
        return parishRepo.findByName(name)
                .stream()
                .map(parishEntity -> {
                    ParishRes parishRes = new ParishRes();
                    parishRes.setId(parishEntity.getId());
                    parishRes.setName(parishEntity.getName());
                    parishRes.setDividedOtherCounty(parishEntity.getDividedOtherCounty());

                    MunicipalityEntity municipalityEntity = parishEntity.getMunicipalityEntity();

                    if (municipalityEntity != null) {
                        MunicipalityRes municipalityRes = new MunicipalityRes();
                        municipalityRes.setId(municipalityEntity.getId());
                        municipalityRes.setName(municipalityEntity.getName());

                        parishRes.setMunicipalityRes(municipalityRes);
                    }

                    CountyEntity countyEntity = parishEntity.getCountyEntity();

                    if (countyEntity != null) {
                        CountyRes countyRes = new CountyRes();
                        countyRes.setId(countyEntity.getId());
                        countyRes.setName(countyEntity.getName());

                        parishRes.setCountyRes(countyRes);
                    }

                    return parishRes;
                })
                .collect(Collectors.toList());

    }


}
