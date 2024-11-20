package se.ki.education.nkcx.controller.extHpv;

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
import se.ki.education.nkcx.dto.request.ExtHpvReq;
import se.ki.education.nkcx.dto.request.HpvReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.ExtHpvRes;
import se.ki.education.nkcx.dto.response.HpvRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.ExtHpvService;
import se.ki.education.nkcx.service.general.ExtHpvServiceImpl;
import se.ki.education.nkcx.service.general.PersonExtHpvService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/extHpv")
@Api(tags = "ExtHpv", description = "APIs related to ExtHpv.")
public class ExtHpvController {

    private final ExtHpvService extHpvService;
    private final ServiceResponseUtil<ExtHpvRes> extHpvDtoServiceResponseUtil;
    private final ExtHpvServiceImpl extHpvServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;
    private final PersonExtHpvService personExtHpvService;

    public ExtHpvController(ExtHpvService extHpvService, ServiceResponseUtil<ExtHpvRes> extHpvDtoServiceResponseUtil, ExtHpvServiceImpl extHpvServiceImpl, ServiceResponseUtil serviceResponseUtil, PersonExtHpvService personExtHpvService) {
        this.extHpvService = extHpvService;
        this.extHpvDtoServiceResponseUtil = extHpvDtoServiceResponseUtil;
        this.extHpvServiceImpl = extHpvServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
        this.personExtHpvService = personExtHpvService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new ExtHpv")
    public ResponseEntity<ServiceRes> save(@RequestBody ExtHpvReq extHpvReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("ExtHpv Saved.")
                        .addData("ExtHpv", extHpvService.save(extHpvReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all ExtHpv")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq paginationReq) {
        return ResponseEntity.ok(extHpvDtoServiceResponseUtil.buildServiceResponse(
                        true,
                        "ExtHpv retrieved.",
                        extHpvService.get(paginationReq)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update a new ExtHpv")
    public ResponseEntity<ServiceRes> update(@RequestBody ExtHpvReq extHpvReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("ExtHpv updated.")
                        .addData("ExtHpv", extHpvService.update(extHpvReq))
        );
    }

    @DeleteMapping(value = "/{extHpvId}")
    @ApiOperation(value = "Delete n existing ExtHpv.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long extHpvId) {
        extHpvService.delete(extHpvId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("extHpv Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for extHpv.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Hpv");
            String[] headersArr = {
                    "Person Id", "ExtHpv Name"

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
            headers.setContentDispositionFormData("attachment", "extHpv_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }


    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Hpv data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        extHpvServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );

    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Hpv data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = extHpvServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "person_ext_hpv_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    @ApiOperation("Search for extHpv based on name.")
    public ResponseEntity<List<ExtHpvRes>> findHpvByName(@RequestParam String name) {
        List<ExtHpvRes> extHpvResList = extHpvService.findExtHpvByName(name);
        return extHpvResList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(extHpvResList);
    }
}
