package se.ki.education.nkcx.controller.referenceType;

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
import se.ki.education.nkcx.dto.request.InvitationTypeReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.ReferenceTypeReq;
import se.ki.education.nkcx.dto.response.InvitationTypeRes;
import se.ki.education.nkcx.dto.response.ReferenceTypeRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.InvitationTypeService;
import se.ki.education.nkcx.service.general.InvitationTypeServiceImpl;
import se.ki.education.nkcx.service.general.ReferenceTypeService;
import se.ki.education.nkcx.service.general.ReferenceTypeServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/referenceType")
@Api(tags = "ReferenceType", description = "APIs related to referenceType.")
public class ReferenceTypeController {

    private final ReferenceTypeService referenceTypeService;
    private final ServiceResponseUtil<ReferenceTypeRes> referenceDtoServiceResponseUtil;
    private final ReferenceTypeServiceImpl referenceTypeServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public ReferenceTypeController(ReferenceTypeService referenceTypeService, ServiceResponseUtil<ReferenceTypeRes> referenceDtoServiceResponseUtil, ReferenceTypeServiceImpl referenceTypeServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.referenceTypeService = referenceTypeService;
        this.referenceDtoServiceResponseUtil = referenceDtoServiceResponseUtil;
        this.referenceTypeServiceImpl = referenceTypeServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new ReferenceType.")
    public ResponseEntity<ServiceRes> save(@RequestBody ReferenceTypeReq referenceTypeReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("ReferenceType saved.")
                        .addData("referenceType", referenceTypeService.save(referenceTypeReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all ReferenceType")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(referenceDtoServiceResponseUtil.buildServiceResponse(
                true,
                "ReferenceType retrieved",
                referenceTypeService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new ReferenceType")
    public ResponseEntity<ServiceRes> update(@RequestBody ReferenceTypeReq referenceTypeReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("ReferenceType updated.")
                        .addData("ReferenceType", referenceTypeService.update(referenceTypeReq))
        );
    }

    @DeleteMapping(value = "/{referenceTypeId}")
    @ApiOperation(value = "Delete an existing ReferenceType.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long referenceTypeId) {
        referenceTypeService.delete(referenceTypeId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("ReferenceType Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for ReferenceType.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("ReferenceType");
            String[] headersArr = {
                    "Type"

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
            headers.setContentDispositionFormData("attachment", "ReferenceType_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import  data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        referenceTypeServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export ReferenceType data")
    public ResponseEntity<byte[]> export(
    ) throws IOException {


        Workbook workbook = referenceTypeServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "ReferenceType_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    public ResponseEntity<List<ReferenceTypeRes>> getReferenceByType(@RequestParam String type) {
        List<ReferenceTypeRes> referenceTypeRes = referenceTypeService.findReferenceTypeByName(type);
        return ResponseEntity.ok(referenceTypeRes);
    }
}
