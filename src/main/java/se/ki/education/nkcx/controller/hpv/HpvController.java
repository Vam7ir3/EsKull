package se.ki.education.nkcx.controller.hpv;

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
import se.ki.education.nkcx.dto.request.HpvReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.HpvRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.HpvService;
import se.ki.education.nkcx.service.general.HpvServiceImpl;
import se.ki.education.nkcx.service.general.PersonHpvService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/hpv")
@Api(tags = "Hpv", description = "APIs related to Hpv.")
public class HpvController {

    private final HpvService hpvService;
    private final ServiceResponseUtil<HpvRes> hpvDtoServiceResponseUtil;
    private final HpvServiceImpl hpvServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;
    private final PersonHpvService personHpvService;


    public HpvController(HpvService hpvService, ServiceResponseUtil<HpvRes> hpvDtoServiceResponseUtil, HpvServiceImpl hpvServiceImpl, ServiceResponseUtil serviceResponseUtil, PersonHpvService personHpvService) {
        this.hpvService = hpvService;
        this.hpvDtoServiceResponseUtil = hpvDtoServiceResponseUtil;
        this.hpvServiceImpl = hpvServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
        this.personHpvService = personHpvService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Hpv")
    public ResponseEntity<ServiceRes> save(@RequestBody HpvReq hpvReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Hpv Saved.")
                        .addData("Hpv", hpvService.save(hpvReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Hpv")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq paginationReq) {
        return ResponseEntity.ok(hpvDtoServiceResponseUtil.buildServiceResponse(
                        true,
                        "Hpv retrieved.",
                        hpvService.get(paginationReq)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update a new Hpv")
    public ResponseEntity<ServiceRes> update(@RequestBody HpvReq hpvReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Hpv updated.")
                        .addData("Hpv", hpvService.update(hpvReq))
        );
    }

    @DeleteMapping(value = "/{hpvId}")
    @ApiOperation(value = "Delete an existing Hpv.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long hpvId) {
        hpvService.delete(hpvId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("hpv Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for hpv.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Hpv");
            String[] headersArr = {
                    "Person Id", "Hpv Name"

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
            headers.setContentDispositionFormData("attachment", "hpv_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }


    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Hpv data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        hpvServiceImpl.importData(multipartFile);
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


        Workbook workbook = hpvServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "person_hpv_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    @ApiOperation("Search for hpv based on name.")
    public ResponseEntity<List<HpvRes>> findHpvByName(@RequestParam String name) {
        List<HpvRes> hpvResList = hpvService.findHpvByName(name);
        return hpvResList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(hpvResList);
    }
}
