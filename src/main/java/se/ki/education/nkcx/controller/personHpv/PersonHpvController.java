package se.ki.education.nkcx.controller.personHpv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PersonHpvRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.PersonHpvService;

import java.util.List;

@RestController
@RequestMapping("/api/person_hpv")
@Api(tags = "PersonHpv", description = "APIs related to PersonHpv.")
public class PersonHpvController {

    @Autowired
    private final PersonHpvService personHpvService;
    private final ServiceResponseUtil<PersonHpvRes> personHpvDtoServiceResponseUtil;

    public PersonHpvController(PersonHpvService personHpvService, ServiceResponseUtil<PersonHpvRes> personSampleDtoServiceResponseUtil) {
        this.personHpvService = personHpvService;
        this.personHpvDtoServiceResponseUtil = personSampleDtoServiceResponseUtil;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonHpvRes> getPersonHpv(@PathVariable Long id) {
        PersonHpvRes personHpvRes = personHpvService.getById(id);
        return ResponseEntity.ok(personHpvRes);
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Person Hpv")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(personHpvDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Person Hpv retrieved",
                personHpvService.get(pagination)
        ));
    }

//    @GetMapping
//    public ResponseEntity<List<PersonHpvRes>> getAllPersonHpvs() {
//        List<PersonHpvRes> personHpvResList = personHpvService.getAll();
//        return ResponseEntity.ok(personHpvResList);
//    }

    @GetMapping("/filter")
    @ApiOperation(value = "Filter based on person.")
    public ResponseEntity<List<PersonHpvRes>> filterByPerson(
            @RequestParam(required = false) List<Long> personId) {

        // Call the service method
        List<PersonHpvRes> personHpvRes = personHpvService.filterByPerson(personId);

        return ResponseEntity.ok(personHpvRes);
    }

    @GetMapping("/search")
    @ApiOperation("Search for persons based on Hpv.")
    public ResponseEntity<List<PersonHpvRes>> searchByHpvName(@RequestParam String name) {
        List<PersonHpvRes> personHpvRes = personHpvService.searchByHpvName(name);
        return personHpvRes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(personHpvRes);
    }
}
