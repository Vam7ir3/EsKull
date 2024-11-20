package se.ki.education.nkcx.controller.klartext;

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
import se.ki.education.nkcx.dto.request.KlartextReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.KlartextRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.KlartextService;
import se.ki.education.nkcx.service.general.KlartextServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/klartext")
@Api(tags = "Klartext", description = "APIs related to klartext.")
public class KlartextController {

    private final KlartextService klartextService;
    private final ServiceResponseUtil<KlartextRes> klartextDtoServiceResponseUtil;
    private final KlartextServiceImpl klartextServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public KlartextController(KlartextService klartextService, ServiceResponseUtil<KlartextRes> klartextDtoServiceResponseUtil, KlartextServiceImpl klartextServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.klartextService = klartextService;
        this.klartextDtoServiceResponseUtil = klartextDtoServiceResponseUtil;
        this.klartextServiceImpl = klartextServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Klartext.")
    public ResponseEntity<ServiceRes> save(@RequestBody KlartextReq klartextReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Klartext saved.")
                        .addData("klartext", klartextService.save(klartextReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Klartext")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(klartextDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Klartext retrieved",
                klartextService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update Klartext")
    public ResponseEntity<ServiceRes> update(@RequestBody KlartextReq klartextReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Klartext updated.")
                        .addData("klartext", klartextService.update(klartextReq))
        );
    }

    @DeleteMapping(value = "/{klartextId}")
    @ApiOperation(value = "Delete an existing klartext.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long klartextId) {
        klartextService.delete(klartextId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("klartext Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for klartext.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("klartext");
            String[] headersArr = {
                    "snomedText"

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
            headers.setContentDispositionFormData("attachment", "Klartext_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import klartext data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        klartextServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export klartext data")
    public ResponseEntity<byte[]> export(
    ) throws IOException {


        Workbook workbook = klartextServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "klartext_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    public ResponseEntity<List<KlartextRes>> getKlartextByName(@RequestParam String snomedText) {
        List<KlartextRes> klartextRes = klartextService.findKlartextByName(snomedText);
        return ResponseEntity.ok(klartextRes);
    }
}
