package se.ki.education.nkcx.controller.personExtHpv;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.response.PersonExtHpvRes;
import se.ki.education.nkcx.service.general.PersonExtHpvService;

import java.util.List;

@RestController
@RequestMapping("/api/person_extHpv")
@Api(tags = "PersonExtHpv", description = "APIs related to PersonExtHpv.")
public class PersonExtHpvController {

    @Autowired
    PersonExtHpvService personExtHpvService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonExtHpvRes> getPersonExtHpv(@PathVariable Long id) {
        PersonExtHpvRes personExtHpvRes = personExtHpvService.getById(id);
        return ResponseEntity.ok(personExtHpvRes);
    }

    @GetMapping
    public ResponseEntity<List<PersonExtHpvRes>> getAllPersonExtHpvs() {
        List<PersonExtHpvRes> personExtHpvResList = personExtHpvService.getAll();
        return ResponseEntity.ok(personExtHpvResList);
    }

    @GetMapping("/filter")
    @ApiOperation(value = "Filter based on person.")
    public ResponseEntity<List<PersonExtHpvRes>> filterByPerson(
            @RequestParam(required = false) List<Long> personId) {

        // Call the service method
        List<PersonExtHpvRes> personExtHpvRes = personExtHpvService.filterByPerson(personId);

        return ResponseEntity.ok(personExtHpvRes);
    }

    @GetMapping("/search")
    @ApiOperation("Search for persons based on ExtHpv.")
    public ResponseEntity<List<PersonExtHpvRes>> searchByExtHpvName(@RequestParam String name) {
        List<PersonExtHpvRes> personExtHpvRes = personExtHpvService.searchByExtHpvName(name);
        return personExtHpvRes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(personExtHpvRes);
    }
}
