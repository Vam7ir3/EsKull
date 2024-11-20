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
import se.ki.education.nkcx.dto.request.InvitationTypeReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.InvitationTypeRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.InvitationTypeEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.InvitationTypeRepo;
import se.ki.education.nkcx.util.DataValidationForExcelImport;
import se.ki.education.nkcx.util.excelExport.InvitationTypeExcelExporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class InvitationTypeServiceImpl implements InvitationTypeService {

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private InvitationTypeRepo invitationTypeRepo;

    private final DtoUtil<InvitationTypeEntity, InvitationTypeReq, InvitationTypeRes> dtoUtil;

    private final PaginationDtoUtil<InvitationTypeEntity, InvitationTypeReq, InvitationTypeRes> paginationDtoUtil;

    public InvitationTypeServiceImpl(LogService logService, InvitationTypeRepo invitationTypeRepo, DtoUtil<InvitationTypeEntity, InvitationTypeReq, InvitationTypeRes> dtoUtil, PaginationDtoUtil<InvitationTypeEntity, InvitationTypeReq, InvitationTypeRes> paginationDtoUtil) {
        this.logService = logService;
        this.invitationTypeRepo = invitationTypeRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('INVITATIONTYPE_C')")
    @Override
    public InvitationTypeRes save(InvitationTypeReq invitationTypeReq) {
        LOG.info("----- Saving InvitationType. -----");
        invitationTypeRepo.findByType(invitationTypeReq.getType()).ifPresent(invitationTypeEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.INVITATIONTYPE002.getMessage());
        });
        InvitationTypeEntity entity = dtoUtil.reqToEntity(invitationTypeReq);
        InvitationTypeEntity savedEntity = invitationTypeRepo.save(entity);
        InvitationTypeRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new InvitationTypeEntity", "A new InvitationTypeEntity data is added successfully. ", currentUser);


        return res;
    }

    @Override
    public PaginationRes<InvitationTypeRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting InvitationTypeEntity. -----");

        List<String> validFields = Arrays.asList("type");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'type'", sortBy);
            sortBy = "type";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<InvitationTypeEntity> invitationTypeEntityPage = invitationTypeRepo.findAll(pageable);

        List<InvitationTypeRes> invitationTypeRes = invitationTypeEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(invitationTypeEntityPage, invitationTypeRes);
    }

    @Override
    public InvitationTypeRes update(InvitationTypeReq invitationTypeReq) {
        LOG.info("----- Updating InvitationType. -----");
        Optional<InvitationTypeEntity> optionalInvitationTypeEntity = invitationTypeRepo.findById(invitationTypeReq.getId());
        optionalInvitationTypeEntity.orElseThrow(() -> new CustomException("COU001"));
        InvitationTypeEntity invitationTypeEntity = optionalInvitationTypeEntity.get();
        dtoUtil.setUpdatedValue(invitationTypeReq, invitationTypeEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing InvitationType data", "Existing InvitationType data is modified successfully.", currentUser);

        return dtoUtil.prepRes(invitationTypeEntity);
    }

    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting InvitationType. -----");
        Optional<InvitationTypeEntity> optionalInvitationTypeEntity = invitationTypeRepo.findById(id);
        optionalInvitationTypeEntity.orElseThrow(() -> new CustomException("COU001"));
        invitationTypeRepo.delete(optionalInvitationTypeEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing InvitationType", "Existing InvitationType data is deleted successfully. ", currentUser);

    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing InvitationType Data. ------");

        List<InvitationTypeReq> invitationTypeReqs = extractDataFromFile(multipartFile.getInputStream());

        List<InvitationTypeEntity> invitationTypeEntities = new ArrayList<>();
        for (InvitationTypeReq invitationTypeReq : invitationTypeReqs) {
            InvitationTypeEntity invitationTypeEntity = dtoUtil.reqToEntity(invitationTypeReq);
            invitationTypeEntities.add(invitationTypeEntity);

        }
        return invitationTypeRepo.saveAll(invitationTypeEntities).size() > 0;
    }

    private List<InvitationTypeReq> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<InvitationTypeReq> invitationTypeReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            InvitationTypeReq invitationTypeReq = extractDataFromRow(row);
            invitationTypeReqs.add(invitationTypeReq);
        }

        workbook.close();
        return invitationTypeReqs;
    }

    private InvitationTypeReq extractDataFromRow(Row row) {
        InvitationTypeReq invitationTypeReq = new InvitationTypeReq();

        invitationTypeReq.setType(DataValidationForExcelImport.getCellValueOrDefault(row, 0));
        invitationTypeReq.setXtype(DataValidationForExcelImport.getCellValueOrDefault(row, 1));
        invitationTypeReq.setDescription(DataValidationForExcelImport.getCellValueOrDefault(row, 2));

        return invitationTypeReq;
    }

    @Override
    public Workbook exportFile() {
        String sortBy = "name";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        InvitationTypeExcelExporter invitationTypeExcelExporter = new InvitationTypeExcelExporter();
        List<InvitationTypeEntity> invitationTypeEntities = invitationTypeRepo.findAll(Sort.by(sortOrder, sortBy));
        List<InvitationTypeRes> feedbackResDtos = invitationTypeEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());
        return invitationTypeExcelExporter.exportToExcel(feedbackResDtos);
    }

    @Override
    public List<InvitationTypeRes> findInvitationTypeByName(String type) {
        return invitationTypeRepo.findByInvitationContaining(type).stream()
                .map(invitationTypeEntity -> {
                    InvitationTypeRes invitationTypeRes = new InvitationTypeRes();
                    invitationTypeRes.setId(invitationTypeEntity.getId());
                    invitationTypeRes.setType(invitationTypeEntity.getType());
                    invitationTypeRes.setXtype(invitationTypeEntity.getXtype());
                    invitationTypeRes.setDescription(invitationTypeEntity.getDescription());

                    return invitationTypeRes;
                })
                .collect(Collectors.toList());
    }
}
