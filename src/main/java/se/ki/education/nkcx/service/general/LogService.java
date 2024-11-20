package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.ki.education.nkcx.config.AuthenticatedUser;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.LogEntity;
import se.ki.education.nkcx.entity.RoleEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.LogRepo;
import se.ki.education.nkcx.repo.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.ki.education.nkcx.util.excelExport.LogExcelExporter;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LogService {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private LogRepo logRepo;

    @Autowired
    private UserRepo userRepo;

    private final PaginationDtoUtil<LogEntity, LogRes, LogService> dtoUtil;

    public LogService(PaginationDtoUtil<LogEntity, LogRes, LogService> dtoUtil) {
        this.dtoUtil = dtoUtil;
    }

    @Transactional
    public void logActivity(String operation, String description, UserEntity user) {
        if (description == null || operation == null || user == null) {
            throw new CustomException("ERR006");
        }

        LOG.info("----- Logging user activity -----");

        LogEntity logEntity = new LogEntity();
        logEntity.setOperation(operation);
        logEntity.setDescription(description);
        logEntity.setUserEntity(user);
        logEntity.setTimestamp(LocalDateTime.now());

        logRepo.save(logEntity);
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            return userRepo.findById(authenticatedUser.getId()).orElse(null);
        }
        return null;
    }

    @PreAuthorize("hasAnyAuthority('LOG_RA')")
    public List<LogRes> getAllLogs() {
        return logRepo.findAllByOrderByTimestampDesc().stream()
                .map(log -> {
                    LogRes logRes = new LogRes();
                    logRes.setId(log.getId());
                    logRes.setDescription(log.getDescription());
                    logRes.setOperation(log.getOperation());
                    // Convert UserEntity to UserResDto
                    UserResDto userResDto = new UserResDto();
                    userResDto.setId(log.getUserEntity().getId());
                    userResDto.setFirstName(log.getUserEntity().getFirstName());
                    userResDto.setLastName(log.getUserEntity().getLastName());
                    userResDto.setEmailAddress(log.getUserEntity().getEmailAddress());
                    // Set UserResDto to LogRes
                    logRes.setUserResDto(userResDto);
                    logRes.setTimestamp(log.getTimestamp());
                    return logRes;
                })
                .collect(Collectors.toList());
    }

    public Workbook exportFile() {
        String sortBy = "id";
        Sort.Direction sortOrder = Sort.Direction.DESC;

        // Fetch sorted log entities from the repository
        List<LogEntity> logEntities = logRepo.findAll(Sort.by(sortOrder, sortBy));

        // Manually map LogEntity to LogRes DTO
        List<LogRes> logResDtos = logEntities.stream()
                .map(logEntity -> {
                    LogRes logRes = new LogRes();
                    logRes.setId(logEntity.getId());
                    logRes.setDescription(logEntity.getDescription());
                    logRes.setOperation(logEntity.getOperation());
                    logRes.setTimestamp(logEntity.getTimestamp());

                    // Map UserEntity to UserResDto
                    if (logEntity.getUserEntity() != null) {
                        UserResDto userResDto = new UserResDto();
                        userResDto.setId(logEntity.getUserEntity().getId());
                        userResDto.setFirstName(logEntity.getUserEntity().getFirstName());
                        userResDto.setLastName(logEntity.getUserEntity().getLastName());
                        userResDto.setEmailAddress(logEntity.getUserEntity().getEmailAddress());
                        logRes.setUserResDto(userResDto);
                    }

                    return logRes;
                })
                .collect(Collectors.toList());

        // Create an Excel exporter and generate the workbook
        LogExcelExporter logExcelExporter = new LogExcelExporter();
        return logExcelExporter.exportToExcel(logResDtos);
    }

    public List<LogRes> findLogByEmail(String emailAddress) {
        return logRepo.findLogsByUserEmailContaining(emailAddress).stream()
                .map(logEntity -> {
                    LogRes logRes = new LogRes();
                    logRes.setId(logEntity.getId());
                    logRes.setDescription(logEntity.getDescription());
                    logRes.setOperation(logEntity.getOperation());

                    // Map UserEntity to UserResDto
                    if (logEntity.getUserEntity() != null) {
                        UserResDto userResDto = new UserResDto();
                        userResDto.setId(logEntity.getUserEntity().getId());
                        userResDto.setFirstName(logEntity.getUserEntity().getFirstName());
                        userResDto.setLastName(logEntity.getUserEntity().getLastName());
                        userResDto.setEmailAddress(logEntity.getUserEntity().getEmailAddress());
                        logRes.setUserResDto(userResDto);
                    }

                    logRes.setTimestamp(logEntity.getTimestamp());
                    return logRes;
                })
                .collect(Collectors.toList());
    }

    public ServiceRes filterLogsByUserId(Long userId, int page, int size, String sortBy, String sortDirection) {
        try {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

            Page<LogEntity> logPage = logRepo.findByUserEntity_Id(userId, pageable);

            List<LogRes> logResList = logPage.getContent().stream()
                    .map(this::convertToLogRes)
                    .collect(Collectors.toList());

            PaginationRes paginationRes = new PaginationRes(
            );

            return new ServiceRes()
                    .setStatus(true)
                    .setMessage("Logs filtered successfully")
                    .addData("logs", logResList)
                    .addData("pagination", paginationRes);

        } catch (Exception e) {
            LOG.error("Error filtering logs by user ID: {}", e.getMessage());
            return new ServiceRes()
                    .setStatus(false)
                    .setMessage("Error occurred while filtering logs");
        }
    }


    private LogRes convertToLogRes(LogEntity logEntity) {
        LogRes logRes = new LogRes();
        logRes.setId(logEntity.getId());
        logRes.setDescription(logEntity.getDescription());
        logRes.setOperation(logEntity.getOperation());
        logRes.setTimestamp(logEntity.getTimestamp());

        if (logEntity.getUserEntity() != null) {
            UserResDto userResDto = new UserResDto();
            userResDto.setId(logEntity.getUserEntity().getId());
            userResDto.setFirstName(logEntity.getUserEntity().getFirstName());
            userResDto.setLastName(logEntity.getUserEntity().getLastName());
            userResDto.setEmailAddress(logEntity.getUserEntity().getEmailAddress());
            logRes.setUserResDto(userResDto);
        }
        return logRes;
    }
}
