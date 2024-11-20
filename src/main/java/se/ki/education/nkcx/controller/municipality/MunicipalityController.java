package se.ki.education.nkcx.controller.municipality;

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
import se.ki.education.nkcx.dto.response.MunicipalityRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.MunicipalityService;
import se.ki.education.nkcx.service.general.PersonServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/municipality")
@Api(tags = "Municipality", description = "APIs related to Municipality.")
public class MunicipalityController {

    private final MunicipalityService municipalityService;
    private final ServiceResponseUtil<MunicipalityRes> municipalityDtoServiceResponseUtil;
    private final PersonServiceImpl personServiceImpl;

    public MunicipalityController(MunicipalityService municipalityService, ServiceResponseUtil<MunicipalityRes> municipalityDtoServiceResponseUtil, PersonServiceImpl personServiceImpl) {
        this.municipalityService = municipalityService;
        this.municipalityDtoServiceResponseUtil = municipalityDtoServiceResponseUtil;
        this.personServiceImpl = personServiceImpl;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Municipality.")
    public ResponseEntity<ServiceRes> save(@RequestBody MunicipalityReq municipalityReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Municipality saved.")
                        .addData("Municipality", municipalityService.save(municipalityReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Municipality")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(municipalityDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Municipality retrieved",
                municipalityService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new Municipality")
    public ResponseEntity<ServiceRes> update(@RequestBody MunicipalityReq municipalityReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Municipality updated.")
                        .addData("Municipality", municipalityService.update(municipalityReq))
        );
    }

    @DeleteMapping(value = "/{municipalityId}")
    @ApiOperation(value = "Delete an existing Municipality.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long municipalityId) {
        municipalityService.delete(municipalityId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Municipality Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for Municipality.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Municipality");
            String[] headersArr = {
                    "Id", "Name", "Year"

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
            headers.setContentDispositionFormData("attachment", "municipality_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }


    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Municipality data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        municipalityService.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Municipality data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = municipalityService.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "municipality_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    @ApiOperation("Search for municipality based on name.")
    public ResponseEntity<List<MunicipalityRes>> searchByMunicipalityName(@RequestParam String name) {
        List<MunicipalityRes> municipalityRes = municipalityService.findMunicipalityByName(name);
        return municipalityRes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(municipalityRes);
    }
}
