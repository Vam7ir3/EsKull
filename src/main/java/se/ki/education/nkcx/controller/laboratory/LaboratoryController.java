package se.ki.education.nkcx.controller.laboratory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.LaboratoryReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.LaboratoryRes;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.LaboratoryService;
import se.ki.education.nkcx.service.general.LaboratoryServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/laboratory")
@Api(tags = "Laboratory", description = "APIs related to Laboratory.")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;
    private final ServiceResponseUtil<LaboratoryRes> laboratoryDtoServiceResponseUtil;
    private final LaboratoryServiceImpl laboratoryServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public LaboratoryController(LaboratoryService laboratoryService, ServiceResponseUtil<LaboratoryRes> laboratoryDtoServiceResponseUtil, LaboratoryServiceImpl laboratoryServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.laboratoryService = laboratoryService;
        this.laboratoryDtoServiceResponseUtil = laboratoryDtoServiceResponseUtil;
        this.laboratoryServiceImpl = laboratoryServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Laboratory.")
    public ResponseEntity<ServiceRes> save(@RequestBody LaboratoryReq laboratoryReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Laboratory saved.")
                        .addData("laboratory", laboratoryService.save(laboratoryReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Laboratory")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(laboratoryDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Laboratory retrieved",
                laboratoryService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new Laboratory")
    public ResponseEntity<ServiceRes> update(@RequestBody LaboratoryReq laboratoryReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Laboratory updated.")
                        .addData("laboratory", laboratoryService.update(laboratoryReq))
        );
    }

    @DeleteMapping(value = "/{laboratoryId}")
    @ApiOperation(value = "Delete an existing Laboratory.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long laboratoryId) {
        laboratoryService.delete(laboratoryId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Laboratory Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for Laboratory.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Laboratory");
            String[] headersArr = {
                    "Name", "IsInUse", "SosLab", "SosLabName", "SosLongName", "Region"

            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < headersArr.length; i++) {
                Cell headerCell = header.createCell(i);
                headerCell.setCellValue(headersArr[i]);
            }

            workbook.write(byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Laboratory_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }


    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Person data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        laboratoryServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );

    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Laboratory data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = laboratoryServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Laboratory_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    public ResponseEntity<List<LaboratoryRes>> getLaboratoryByName(@RequestParam String name) {
        List<LaboratoryRes> laboratoryRes = laboratoryService.findLaboratoryByName(name);
        return ResponseEntity.ok(laboratoryRes);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<LaboratoryRes>> filterIsInUse(
            @RequestParam Boolean isInUse) {

        // Call the service method
        List<LaboratoryRes> laboratoryRes = laboratoryService.filterIsInUse(isInUse);

        return ResponseEntity.ok(laboratoryRes);
    }
}
