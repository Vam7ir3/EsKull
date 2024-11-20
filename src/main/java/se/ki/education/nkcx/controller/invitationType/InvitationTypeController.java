package se.ki.education.nkcx.controller.invitationType;

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
import se.ki.education.nkcx.dto.response.InvitationTypeRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.InvitationTypeService;
import se.ki.education.nkcx.service.general.InvitationTypeServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/invitationType")
@Api(tags = "InvitationType", description = "APIs related to invitationType.")
public class InvitationTypeController {

    private final InvitationTypeService invitationTypeService;
    private final ServiceResponseUtil<InvitationTypeRes> invitationDtoServiceResponseUtil;
    private final InvitationTypeServiceImpl invitationTypeServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public InvitationTypeController(InvitationTypeService invitationTypeService, ServiceResponseUtil<InvitationTypeRes> invitationDtoServiceResponseUtil, InvitationTypeServiceImpl invitationTypeServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.invitationTypeService = invitationTypeService;
        this.invitationDtoServiceResponseUtil = invitationDtoServiceResponseUtil;
        this.invitationTypeServiceImpl = invitationTypeServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new InvitationType.")
    public ResponseEntity<ServiceRes> save(@RequestBody InvitationTypeReq invitationTypeReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("InvitationType saved.")
                        .addData("invitationType", invitationTypeService.save(invitationTypeReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all InvitationType")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(invitationDtoServiceResponseUtil.buildServiceResponse(
                true,
                "InvitationType retrieved",
                invitationTypeService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new InvitationType")
    public ResponseEntity<ServiceRes> update(@RequestBody InvitationTypeReq invitationTypeReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("InvitationType updated.")
                        .addData("invitationType", invitationTypeService.update(invitationTypeReq))
        );
    }

    @DeleteMapping(value = "/{invitationTypeId}")
    @ApiOperation(value = "Delete an existing InvitationType.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long invitationTypeId) {
        invitationTypeService.delete(invitationTypeId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("InvitationType Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for InvitationType.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("InvitationType");
            String[] headersArr = {
                    "Type", "XType"

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
            headers.setContentDispositionFormData("attachment", "InvitationType_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }
    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import  data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        invitationTypeServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export InvitationType data")
    public ResponseEntity<byte[]> export(
    ) throws IOException {


        Workbook workbook = invitationTypeServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "InvitationType_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    public ResponseEntity<List<InvitationTypeRes>> getInvitationByType(@RequestParam String type) {
        List<InvitationTypeRes> invitationTypeRes = invitationTypeService.findInvitationTypeByName(type);
        return ResponseEntity.ok(invitationTypeRes);
    }
}
