package se.ki.education.nkcx.controller.district;

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
import se.ki.education.nkcx.dto.request.DistrictReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.DistrictRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.DistrictService;
import se.ki.education.nkcx.service.general.DistrictServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/district")
@Api(tags = "District", description = "APIs related to district.")
public class DistrictController {

    private final DistrictService districtService;
    private final ServiceResponseUtil<DistrictRes> districtDtoServiceResponseUtil;
    private final DistrictServiceImpl districtServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public DistrictController(DistrictService districtService, ServiceResponseUtil<DistrictRes> districtDtoServiceResponseUtil, DistrictServiceImpl districtServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.districtService = districtService;
        this.districtDtoServiceResponseUtil = districtDtoServiceResponseUtil;
        this.districtServiceImpl = districtServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new District.")
    public ResponseEntity<ServiceRes> save(@RequestBody DistrictReq districtReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("District saved.")
                        .addData("district", districtService.save(districtReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all District")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(districtDtoServiceResponseUtil.buildServiceResponse(
                true,
                "District retrieved",
                districtService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new District")
    public ResponseEntity<ServiceRes> update(@RequestBody DistrictReq districtReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("District updated.")
                        .addData("district", districtService.update(districtReq))
        );
    }

    @DeleteMapping(value = "/{districtId}")
    @ApiOperation(value = "Delete an existing District.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long districtId) {
        districtService.delete(districtId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("District Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for District.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("District");
            String[] headersArr = {
                    "Id", "District", "District_Name"

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
            headers.setContentDispositionFormData("attachment", "District_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import District data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        districtServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export District data")
    public ResponseEntity<byte[]> export(
    ) throws IOException {


        Workbook workbook = districtServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "District_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    public ResponseEntity<List<DistrictRes>> getDistrictByName(@RequestParam String district) {
        List<DistrictRes> districtRes = districtService.findDistrictByName(district);
        return ResponseEntity.ok(districtRes);
    }
}
