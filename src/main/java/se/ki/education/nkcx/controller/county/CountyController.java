package se.ki.education.nkcx.controller.county;

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
import se.ki.education.nkcx.dto.request.CountyReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CountyRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.CountyService;
import se.ki.education.nkcx.service.general.CountyServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/county")
@Api(tags = "County", description = "APIs related to County.")
public class CountyController {

    private final CountyService countyService;
    private final ServiceResponseUtil<CountyRes> countyDtoServiceResponseUtil;
    private final CountyServiceImpl countyServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public CountyController(CountyService countyService, ServiceResponseUtil<CountyRes> countyDtoServiceResponseUtil, CountyServiceImpl countyServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.countyService = countyService;
        this.countyDtoServiceResponseUtil = countyDtoServiceResponseUtil;
        this.countyServiceImpl = countyServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new County.")
    public ResponseEntity<ServiceRes> save(@RequestBody CountyReq countyReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("County saved.")
                        .addData("county", countyService.save(countyReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all County")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(countyDtoServiceResponseUtil.buildServiceResponse(
                true,
                "County retrieved",
                countyService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new County")
    public ResponseEntity<ServiceRes> update(@RequestBody CountyReq countyReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("County updated.")
                        .addData("county", countyService.update(countyReq))
        );
    }

    @DeleteMapping(value = "/{countyId}")
    @ApiOperation(value = "Delete an existing County.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long countyId) {
        countyService.delete(countyId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("County Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for County.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("County");
            String[] headersArr = {
                    "Name"

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
            headers.setContentDispositionFormData("attachment", "County_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Person data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        countyServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export County data")
    public ResponseEntity<byte[]> export(
    ) throws IOException {


        Workbook workbook = countyServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "County_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    public ResponseEntity<List<CountyRes>> getCountyByName(@RequestParam String name) {
        List<CountyRes> countyRes = countyService.findCountyByName(name);
        return ResponseEntity.ok(countyRes);
    }

}
