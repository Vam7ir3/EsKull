package se.ki.education.nkcx.controller.person;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.service.general.PersonService;
import se.ki.education.nkcx.service.general.PersonServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hibernate.id.SequenceMismatchStrategy.LOG;

@RestController
@RequestMapping(value = "/api/person")
@Api(tags = "Person", description = "APIs related to Person.")
public class PersonController {

    private static final Logger LOG = LogManager.getLogger();
    private final PersonService personService;
    private final ServiceResponseUtil<PersonRes> personDtoServiceResponseUtil;
    private final PersonServiceImpl personServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    @Autowired
    public PersonController(final PersonService personService, ServiceResponseUtil<PersonRes> personDtoServiceResponseUtil, PersonServiceImpl personServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.personService = personService;
        this.personDtoServiceResponseUtil = personDtoServiceResponseUtil;
        this.personServiceImpl = personServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Person.")
    public ResponseEntity<ServiceRes> save(@RequestBody PersonReq personReq) {
        LOG.info("Incoming request to save person: {}", personReq);

        return ResponseEntity.ok(
                 new ServiceRes()
                        .setStatus(true)
                        .setMessage("Person saved.")
                        .addData("person", personService.save(personReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Person")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(personDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Person retrieved",
                personService.get(pagination)
        ));
    }

//    @GetMapping
//    public PaginationRes<PersonRes> getPersons(PaginationReq paginationReq) {
//        return personService.get(paginationReq);
//    }

    @PutMapping
    @ApiOperation(value = "Update a new Person")
    public ResponseEntity<ServiceRes> update(@RequestBody PersonReq personReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                .setStatus(true)
                .setMessage("Person updated.")
                .addData("person", personService.update(personReq))
        );
    }

    @DeleteMapping(value = "/{personId}")
    @ApiOperation(value = "Delete an existing Person.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long personId) {
        personService.delete(personId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Person Deleted")
        );
    }


    @GetMapping(value = "/download_template")
    @ApiOperation(value = "Download template file for Person.")
    public ResponseEntity<byte[]> downloadTemplateFileDirect() throws IOException {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096)) {

            Sheet sheet = workbook.createSheet("Person");
            String[] headersArr = {
                   "DateOfBirth", "Pnr", "IsValidPNR"

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
            headers.setContentDispositionFormData("attachment", "person_template.xlsx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(bytes);
        }


    }

    @PostMapping(value = "/data_import")
    @ApiOperation(value = "Import Person data from excel file")
    public ResponseEntity<ServiceRes> uploadExcelFile(@RequestPart(value = "excelFile") MultipartFile multipartFile) throws IOException {
        personServiceImpl.importData(multipartFile);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Data imported successfully.")
        );
    }

    @GetMapping(value = "/export")
    @ApiOperation(value = "Export Person data")
    public ResponseEntity<byte[]> export(

    ) throws IOException {


        Workbook workbook = personServiceImpl.exportFile( );
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "person_data_export.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);

    }

//    @GetMapping("/search")
//    @ApiOperation("Search for persons based on emails.")
//    public ResponseEntity<List<PersonRes>> searchByEmail(@RequestParam String email) {
//        List<PersonRes> personResList = personService.findPersonByEmail(email);
//        return personResList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(personResList);
//    }

//    @GetMapping("/filter")
//    @ApiOperation(value = "Filter persons based on isValidPNR.")
//    public ResponseEntity<List<PersonRes>> filterByIsValidPNR(
//            @RequestParam Boolean isValidPNR) {
//
//        // Call the service method
//        List<PersonRes> personRes = personService.filterPersonByIsValidPnr(isValidPNR);
//
//        return ResponseEntity.ok(personRes);
//    }

    @GetMapping("/filter")
    @ApiOperation(value = "Filter persons based on isValidPNR.")
    public ResponseEntity<ServiceRes> filterPerson(
            @RequestParam Boolean isValidPNR,
            PaginationReq paginationReq) {

        return ResponseEntity.ok(personDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Persons filtered",
                personService.filterPerson(paginationReq, isValidPNR)

        ));
    }



}
