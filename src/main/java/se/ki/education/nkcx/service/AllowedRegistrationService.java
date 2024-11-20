package se.ki.education.nkcx.service;

import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.AllowedRegistrationReq;
import se.ki.education.nkcx.dto.response.AllowedRegistrationRes;
import se.ki.education.nkcx.dto.util.AllowedRegistrationDtoUtil;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.AllowedRegistrationEntity;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.AllowedRegistrationRepo;
import se.ki.education.nkcx.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('ALLOWED_REGISTRATION')")
@Service
@Transactional(rollbackFor = Exception.class)
public class AllowedRegistrationService {

    private static final Logger LOG = LogManager.getLogger();

    private final AllowedRegistrationRepo allowedRegistrationRepo;
    private final DtoUtil<AllowedRegistrationEntity, AllowedRegistrationReq, AllowedRegistrationRes> dtoUtil;
    private final PaginationDtoUtil<AllowedRegistrationEntity, AllowedRegistrationReq, AllowedRegistrationRes> paginationDtoUtil;

    @Autowired
    public AllowedRegistrationService(AllowedRegistrationRepo allowedRegistrationRepo, AllowedRegistrationDtoUtil allowedRegistrationDtoUtil, PaginationDtoUtil<AllowedRegistrationEntity, AllowedRegistrationReq, AllowedRegistrationRes> paginationDtoUtil) {
        this.allowedRegistrationRepo = allowedRegistrationRepo;
        this.dtoUtil = allowedRegistrationDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('ALLOWED_REGISTRATION_C')")
    public AllowedRegistrationRes save(AllowedRegistrationReq allowedRegistrationReq) {
        LOG.info("----- Saving Allowed Registration. -----");

        if (allowedRegistrationRepo.findByEmail(allowedRegistrationReq.getEmail()).isPresent()) {
            throw new CustomException("USR005");
        }

        return dtoUtil.prepRes(allowedRegistrationRepo.save(dtoUtil.reqToEntity(allowedRegistrationReq)));
    }

    public PaginationRes<AllowedRegistrationRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting Allowed Registration. -----");

        List<String> fields = Arrays.asList("email", "createdDate");
        String sortBy = "email";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, allowedRegistrationRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('ALLOWED_REGISTRATION_U')")
    public AllowedRegistrationRes update(AllowedRegistrationReq allowedRegistrationReq) {
        LOG.info("----- Updating Allowed Registration. -----");

        Optional<AllowedRegistrationEntity> optionalAllowedRegistrationEntity = allowedRegistrationRepo.findById(allowedRegistrationReq.getId());
        optionalAllowedRegistrationEntity.orElseThrow(() -> new CustomException("USR009"));

        if (allowedRegistrationReq.getEmail() != null && !allowedRegistrationReq.getEmail().equalsIgnoreCase(optionalAllowedRegistrationEntity.get().getEmail())
                && allowedRegistrationRepo.findByEmail(allowedRegistrationReq.getEmail()).isPresent()) {
            throw new CustomException("USR005");
        }

        dtoUtil.setUpdatedValue(allowedRegistrationReq, optionalAllowedRegistrationEntity.get());

        return dtoUtil.prepRes(optionalAllowedRegistrationEntity.get());
    }

    @PreAuthorize("hasAuthority('ALLOWED_REGISTRATION_D')")
    public void delete(Long id) {
        LOG.info("----- Deleting Allowed Registration. -----");

        Optional<AllowedRegistrationEntity> optionalAllowedRegistrationEntity = allowedRegistrationRepo.findById(id);
        optionalAllowedRegistrationEntity.orElseThrow(() -> new CustomException("USR009"));

        allowedRegistrationRepo.delete(optionalAllowedRegistrationEntity.get());
    }

    @PreAuthorize("hasAuthority('ALLOWED_REGISTRATION_C')")
    public boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("----- Importing Allowed Registration Data. -----");

        List<AllowedRegistrationReq> allowedRegistrationReqs = extractDataFromExcelFile(multipartFile);
        List<AllowedRegistrationReq> unique = removeDuplicate(allowedRegistrationReqs);
        List<AllowedRegistrationEntity> allowedRegistrations = unique.stream().map(dtoUtil::reqToEntity).collect(Collectors.toList());
        return allowedRegistrationRepo.saveAll(allowedRegistrations).size() > 0;
    }

    private List<AllowedRegistrationReq> extractDataFromExcelFile(MultipartFile multipartFile) throws IOException {
        File file = new File(System.getProperty("catalina.home") + File.separator + "saved_file" + File.separator + multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();

        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        List<AllowedRegistrationReq> allowedRegistrationReqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            AllowedRegistrationReq allowedRegistrationReq = new AllowedRegistrationReq();
            String value = dataFormatter.formatCellValue(sheet.getRow(i).getCell(0));
            if (value != null && !value.isEmpty()) {
                allowedRegistrationReq.setEmail(value);
            }
            allowedRegistrationReqs.add(allowedRegistrationReq);
        }

        workbook.close();

        if (file.exists()) {
            if (file.delete()) {
                LOG.info("----- Temporary Excel file deleted. -----");
            }
        }
        return allowedRegistrationReqs;
    }

    private List<AllowedRegistrationReq> removeDuplicate(List<AllowedRegistrationReq> allowedRegistrationReqs) {
        List<AllowedRegistrationReq> unique = new ArrayList<>();
        for (AllowedRegistrationReq allowedRegistrationReq : allowedRegistrationReqs) {
            boolean exists = false;
            for (AllowedRegistrationReq u : unique) {
                if (u.getEmail().equalsIgnoreCase(allowedRegistrationReq.getEmail())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                unique.add(allowedRegistrationReq);
            }
        }
        return unique;
    }

    public String downloadTemplateFile() throws IOException {
        LOG.info("----- Downloading Template file. -----");
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Emails");

        Row header = sheet.createRow(0);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Email Address");

        return FileUtil.saveExcelFileLocally(workbook, "templates", "allowed_registration_template", "xlsx");
    }
}

