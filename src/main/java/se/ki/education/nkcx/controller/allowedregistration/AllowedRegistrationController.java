package se.ki.education.nkcx.controller.allowedregistration;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.AllowedRegistrationReq;
import se.ki.education.nkcx.dto.response.AllowedRegistrationRes;
import se.ki.education.nkcx.service.AllowedRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/allowed_registration")
@Api(tags = "Allowed Registration")
public class AllowedRegistrationController {

    private final AllowedRegistrationService allowedRegistrationService;
    private final ServiceResponseUtil<AllowedRegistrationRes> allowedRegistrationResServiceResponseUtil;

    @Autowired
    public AllowedRegistrationController(AllowedRegistrationService allowedRegistrationService, ServiceResponseUtil<AllowedRegistrationRes> allowedRegistrationResServiceResponseUtil) {
        this.allowedRegistrationService = allowedRegistrationService;
        this.allowedRegistrationResServiceResponseUtil = allowedRegistrationResServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Allowed Registration.")
    public ResponseEntity<ServiceRes> save(@RequestBody AllowedRegistrationReq allowedRegistrationReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Allowed Registration saved.")
                        .addData("allowedRegistrationReq", allowedRegistrationService.save(allowedRegistrationReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Allowed Registration list. If pagination detail is not provided then all the data will be fetched.")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(allowedRegistrationResServiceResponseUtil.buildServiceResponse(
                true,
                "Allowed Registrations retrieved",
                allowedRegistrationService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Allowed Registration.")
    public ResponseEntity<ServiceRes> update(@RequestBody AllowedRegistrationReq allowedRegistrationReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Allowed Registration updated.")
                        .addData("allowedRegistrationReq", allowedRegistrationService.update(allowedRegistrationReq))
        );
    }

    @DeleteMapping(value = "/{allowedRegistrationReqId}")
    @ApiOperation(value = "Delete an existing Allowed Registration.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long allowedRegistrationReqId) {
        allowedRegistrationService.delete(allowedRegistrationReqId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Allowed Registration deleted.")
        );
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Question data from excel file.")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        allowedRegistrationService.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for allowed registration.")
    public ResponseEntity<ServiceRes> export() throws IOException {
        String savedFile = allowedRegistrationService.downloadTemplateFile();
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .addData("fileUrl", savedFile)
        );
    }
}
