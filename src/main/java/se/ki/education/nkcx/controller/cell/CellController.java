package se.ki.education.nkcx.controller.cell;

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
import se.ki.education.nkcx.dto.request.CellReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CellRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.CellService;
import se.ki.education.nkcx.service.general.CellServiceImpl;
import se.ki.education.nkcx.service.general.PersonCellService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/cell")
@Api(tags = "Cell", description = "APIs related to Cell.")
public class CellController {

    private final CellService cellService;
    private final ServiceResponseUtil<CellRes> cellDtoServiceResponseUtil;
    private final CellServiceImpl cellServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;
    private final PersonCellService personCellService;


    public CellController(CellService cellService, ServiceResponseUtil<CellRes> cellDtoServiceResponseUtil, CellServiceImpl cellServiceImpl, ServiceResponseUtil serviceResponseUtil, PersonCellService personCellService) {
        this.cellService = cellService;
        this.cellDtoServiceResponseUtil = cellDtoServiceResponseUtil;
        this.cellServiceImpl = cellServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
        this.personCellService = personCellService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Cell")
    public ResponseEntity<ServiceRes> save(@RequestBody CellReq cellReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Cell Saved.")
                        .addData("Cell", cellService.save(cellReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Cell")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq paginationReq) {
        return ResponseEntity.ok(cellDtoServiceResponseUtil.buildServiceResponse(
                        true,
                        "Cell retrieved.",
                        cellService.get(paginationReq)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update a new Cell")
    public ResponseEntity<ServiceRes> update(@RequestBody CellReq cellReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Cell updated.")
                        .addData("Cell", cellService.update(cellReq))
        );
    }

    @DeleteMapping(value = "/{cellId}")
    @ApiOperation(value = "Delete an existing Cell.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long cellId) {
        cellService.delete(cellId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("cell Deleted")
        );
    }

    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for cell.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Cell");
            String[] headersArr = {
                    "Person Id", "Cell Name"

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
            headers.setContentDispositionFormData("attachment", "cell_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }


    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Cell data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        cellServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );

    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Cell data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = cellServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "person_cell_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

    @GetMapping("/search")
    @ApiOperation("Search for cell based on name.")
    public ResponseEntity<List<CellRes>> findCellByName(@RequestParam String name) {
        List<CellRes> cellResList = cellService.findCellByName(name);
        return cellResList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(cellResList);
    }
}
