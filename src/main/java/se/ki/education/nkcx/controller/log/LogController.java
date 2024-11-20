package se.ki.education.nkcx.controller.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.LogRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.service.general.LogService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/logs")
@Api(tags = "Log")
public class LogController {


    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserEntity> getCurrentUser() {
        UserEntity currentUser = logService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(currentUser);
    }

    // Get all logs
    @PreAuthorize("hasAnyAuthority('LOG_RA')")
    @GetMapping()
    public ServiceRes getAllLogs() {
        List<LogRes> logs = logService.getAllLogs();
        return new ServiceRes()
                .setStatus(true)
                .setMessage("Logs retrieved successfully.")
                .addData("logs", logs);
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Log data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {

        Workbook workbook = logService.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "log_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    public ResponseEntity<List<LogRes>> getLogsByEmail(@RequestParam String emailAddress) {
        List<LogRes> logs = logService.findLogByEmail(emailAddress);
        return ResponseEntity.ok(logs);
    }


    @GetMapping(value = "/filter/{userId}")
    @ApiOperation(value = "Filter logs based on user ID with pagination")
    public ResponseEntity<ServiceRes> filterByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        ServiceRes response = logService.filterLogsByUserId(userId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }
}



