package se.ki.education.nkcx.controller.personSample;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.PersonSampleService;
import se.ki.education.nkcx.service.general.PersonSampleServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/person_samples")
@Api(tags = "PersonSample", description = "APIs related to PersonSample.")
public class PersonSampleController {

    @Autowired
    private final PersonSampleService personSampleService;
    private final ServiceResponseUtil<PersonSampleRes> personSampleDtoServiceResponseUtil;
    private final PersonSampleServiceImpl personSampleServiceImpl;

    public PersonSampleController(PersonSampleService personSampleService, ServiceResponseUtil<PersonSampleRes> personSampleDtoServiceResponseUtil, PersonSampleServiceImpl personSampleServiceImpl) {
        this.personSampleService = personSampleService;
        this.personSampleDtoServiceResponseUtil = personSampleDtoServiceResponseUtil;
        this.personSampleServiceImpl = personSampleServiceImpl;
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<PersonSampleRes> getPersonSample(@PathVariable Long id) {
//        PersonSampleRes personSampleRes = personSampleService.getById(id);
//        return ResponseEntity.ok(personSampleRes);
//    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Person Sample")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(personSampleDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Person Sample retrieved",
                personSampleService.get(pagination)
        ));
    }


//    @GetMapping
//    public ResponseEntity<List<PersonSampleRes>> getAllPersonSamples() {
//        List<PersonSampleRes> personSampleResList = personSampleService.getAll();
//        return ResponseEntity.ok(personSampleResList);
//    }

    @GetMapping("/filter")
    @ApiOperation(value = "Filter based on person.")
    public ResponseEntity<List<PersonSampleRes>> filterByPerson(
            @RequestParam(required = false) List<Long> personId) {

        // Call the service method
        List<PersonSampleRes> personSampleRes = personSampleService.filterByPerson(personId);

        return ResponseEntity.ok(personSampleRes);
    }

    @GetMapping("/search")
    @ApiOperation("Search for persons based on sample.")
    public ResponseEntity<List<PersonSampleRes>> searchBySampleName(@RequestParam String name) {
        List<PersonSampleRes> personSampleRes = personSampleService.searchBySampleName(name);
        return personSampleRes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(personSampleRes);
    }
}
