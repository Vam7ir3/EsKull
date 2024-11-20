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
import se.ki.education.nkcx.dto.request.KlartextReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.KlartextRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.KlartextEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.KlartextRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.KlartextExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class KlartextServiceImpl implements KlartextService{

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private KlartextRepo klartextRepo;

    private final DtoUtil<KlartextEntity, KlartextReq, KlartextRes> dtoUtil;

    private final PaginationDtoUtil<KlartextEntity, KlartextReq, KlartextRes> paginationDtoUtil;

    public KlartextServiceImpl(LogService logService, KlartextRepo klartextRepo, DtoUtil<KlartextEntity, KlartextReq, KlartextRes> dtoUtil, PaginationDtoUtil<KlartextEntity, KlartextReq, KlartextRes> paginationDtoUtil) {
        this.logService = logService;
        this.klartextRepo = klartextRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('KLARTEXT_C')")
    @Override
    public KlartextRes save(KlartextReq klartextReq) {
        LOG.info("----- Saving Klartext. -----");
        klartextRepo.findBySnomedText(klartextReq.getSnomedText()).ifPresent(klartextEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.KLARTEXT002.getMessage());
        });
        KlartextEntity entity = dtoUtil.reqToEntity(klartextReq);
        KlartextEntity savedEntity = klartextRepo.save(entity);
        KlartextRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Klartext", "A new Klartext data is added successfully. ", currentUser);


        return res;
    }

    @Override
    public PaginationRes<KlartextRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting Klartext. -----");

        List<String> validFields = Arrays.asList("snomedText");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'snomedText'", sortBy);
            sortBy = "snomedText";
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<KlartextEntity> klartextEntityPage = klartextRepo.findAll(pageable);

        List<KlartextRes> klartextRes = klartextEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(klartextEntityPage, klartextRes);
    }

    @Override
    public KlartextRes update(KlartextReq klartextReq) {
        LOG.info("----- Updating Klartext. -----");
        Optional<KlartextEntity> optionalKlartextEntity = klartextRepo.findById(klartextReq.getId());
        optionalKlartextEntity.orElseThrow(() -> new CustomException("COU001"));
        KlartextEntity klartextEntity = optionalKlartextEntity.get();
        dtoUtil.setUpdatedValue(klartextReq, klartextEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing Klartext data", "Existing Klartext data is modified successfully.", currentUser);

        return dtoUtil.prepRes(klartextEntity);
    }

    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Klartext. -----");
        Optional<KlartextEntity> optionalKlartextEntity = klartextRepo.findById(id);
        optionalKlartextEntity.orElseThrow(() -> new CustomException("COU001"));
        klartextRepo.delete(optionalKlartextEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Klartext", "Existing Klartext data is deleted successfully. ", currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Klartext Data. ------");

        List<KlartextReq> klartextReqs = extractDataFromFile(multipartFile.getInputStream());

        List<KlartextEntity> klartextEntities = new ArrayList<>();
        for (KlartextReq klartextReq : klartextReqs) {
            KlartextEntity klartextEntity = dtoUtil.reqToEntity(klartextReq);
            klartextEntities.add(klartextEntity);

        }
        return klartextRepo.saveAll(klartextEntities).size() > 0;
    }

    private List<KlartextReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<KlartextReq> klartextReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            KlartextReq klartextReq = extractDataFromRow(row);
            klartextReqs.add(klartextReq);
        }

        workbook.close();
        return klartextReqs;
    }

    private KlartextReq extractDataFromRow(Row row) {
        KlartextReq klartextReq = new KlartextReq();

        klartextReq.setSnomedText(DataValidationForExcelImport.getCellValueOrDefault(row, 1));

        return klartextReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "snomedText";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        KlartextExcelExporter klartextExcelExporter = new KlartextExcelExporter();
        List<KlartextEntity> klartextEntities = klartextRepo.findAll(Sort.by(sortOrder, sortBy));
        List<KlartextRes> feedbackResDtos = klartextEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return klartextExcelExporter.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<KlartextRes> findKlartextByName(String snomedText) {
        return klartextRepo.findByKlartextContaining(snomedText).stream()
                .map(klartextEntity -> {
                    KlartextRes klartextRes = new KlartextRes();
                    klartextRes.setId(klartextEntity.getId());
                    klartextRes.setSnomedText(klartextEntity.getSnomedText());

                    return klartextRes;
                })
                .collect(Collectors.toList());
    }
}
