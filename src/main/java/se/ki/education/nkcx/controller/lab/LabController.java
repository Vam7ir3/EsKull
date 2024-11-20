package se.ki.education.nkcx.controller.lab;

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
import se.ki.education.nkcx.dto.request.LabReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.LabRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.LabService;
import se.ki.education.nkcx.service.general.LabServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/lab")
@Api(tags = "Lab", description = "APIs related to Lab.")
public class LabController {

    private final LabService labService;
    private final ServiceResponseUtil<LabRes> labDtoServiceResponseUtil;
    private final LabServiceImpl labServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public LabController(LabService labService, ServiceResponseUtil<LabRes> labDtoServiceResponseUtil, LabServiceImpl labServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.labService = labService;
        this.labDtoServiceResponseUtil = labDtoServiceResponseUtil;
        this.labServiceImpl = labServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Lab.")
    public ResponseEntity<ServiceRes> save(@RequestBody LabReq labReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Lab saved.")
                        .addData("lab", labService.save(labReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Lab")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(labDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Lab retrieved",
                labService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new Lab")
    public ResponseEntity<ServiceRes> update(@RequestBody LabReq labReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Lab updated.")
                        .addData("lab", labService.update(labReq))
        );
    }

    @DeleteMapping(value = "/{labId}")
    @ApiOperation(value = "Delete an existing Lab.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long labId) {
        labService.delete(labId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Lab Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for Lab.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Lab");
            String[] headersArr = {
                    "Name", "isInUse"

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
            headers.setContentDispositionFormData("attachment", "Lab_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Person data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        labServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Lab data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = labServiceImpl.exportFile();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Lab_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }
}
