package se.ki.education.nkcx.controller.parish;

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
import se.ki.education.nkcx.dto.request.MunicipalityReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.ParishReq;
import se.ki.education.nkcx.dto.response.MunicipalityRes;
import se.ki.education.nkcx.dto.response.ParishRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.ParishService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/parish")
@Api(tags = "Parish", description = "APIs related to Parish.")
public class ParishController {

    private final ParishService parishService;

    private final ServiceResponseUtil<ParishRes> parishDtoServiceResponseUtil;

    public ParishController(ParishService parishService, ServiceResponseUtil<ParishRes> parishDtoServiceResponseUtil) {
        this.parishService = parishService;
        this.parishDtoServiceResponseUtil = parishDtoServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Parish.")
    public ResponseEntity<ServiceRes> save(@RequestBody ParishReq parishReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Parish saved.")
                        .addData("Parish", parishService.save(parishReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Parish")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(parishDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Parish retrieved",
                parishService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new Parish")
    public ResponseEntity<ServiceRes> update(@RequestBody ParishReq parishReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Parish updated.")
                        .addData("Parish", parishService.update(parishReq))
        );
    }

    @DeleteMapping(value = "/{parishId}")
    @ApiOperation(value = "Delete an existing Parish.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long parishId) {
        parishService.delete(parishId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Parish Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for Parish.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Parish");
            String[] headersArr = {
                   "Id", "Name", "Register_Date", "Divided_Other_County", "Municipality_Id", "County_Id"
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
            headers.setContentDispositionFormData("attachment", "parish_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Parish data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        parishService.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Parish data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = parishService.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "parish_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    @ApiOperation("Search for parish based on name.")
    public ResponseEntity<List<ParishRes>> searchByParishName(@RequestParam String name) {
        List<ParishRes> parishRes = parishService.findParishByName(name);
        return parishRes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(parishRes);
    }
}
