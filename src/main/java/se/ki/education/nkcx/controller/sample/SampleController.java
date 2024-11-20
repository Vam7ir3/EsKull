package se.ki.education.nkcx.controller.sample;

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
import se.ki.education.nkcx.dto.request.SampleReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.SampleRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.SampleService;
import se.ki.education.nkcx.service.general.SampleServiceImpl;
import se.ki.education.nkcx.service.general.PersonSampleService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/sample")
@Api(tags = "Sample", description = "APIs related to Sample.")
public class SampleController {

    private final SampleService sampleService;
    private final ServiceResponseUtil<SampleRes> sampleDtoServiceResponseUtil;
    private final SampleServiceImpl sampleServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;
    private final PersonSampleService personSampleService;


    public SampleController(SampleService sampleService, ServiceResponseUtil<SampleRes> sampleDtoServiceResponseUtil, SampleServiceImpl sampleServiceImpl, ServiceResponseUtil serviceResponseUtil, PersonSampleService personSampleService) {
        this.sampleService = sampleService;
        this.sampleDtoServiceResponseUtil = sampleDtoServiceResponseUtil;
        this.sampleServiceImpl = sampleServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
        this.personSampleService = personSampleService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Sample")
    public ResponseEntity<ServiceRes> save(@RequestBody SampleReq sampleReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Sample Saved.")
                        .addData("Sample", sampleService.save(sampleReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Sample")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq paginationReq) {
        return ResponseEntity.ok(sampleDtoServiceResponseUtil.buildServiceResponse(
                        true,
                        "Sample retrieved.",
                        sampleService.get(paginationReq)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update a new Sample")
    public ResponseEntity<ServiceRes> update(@RequestBody SampleReq sampleReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Sample updated.")
                        .addData("Sample", sampleService.update(sampleReq))
        );
    }

    @DeleteMapping(value = "/{sampleId}")
    @ApiOperation(value = "Delete an existing Sample.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long sampleId) {
        sampleService.delete(sampleId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("sample Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for sample.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Sample");
            String[] headersArr = {
                    "Person Id", "Sample Name"

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
            headers.setContentDispositionFormData("attachment", "Sample_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }


    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Sample data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        sampleServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );

    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Sample data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = sampleServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "person_Sample_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    @ApiOperation("Search for sample based on name.")
    public ResponseEntity<List<SampleRes>> findSampleByName(@RequestParam String type) {
        List<SampleRes> sampleResList = sampleService.findSampleByName(type);
        return sampleResList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(sampleResList);
    }
}
