package se.ki.education.nkcx.controller.personCell;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonCellRes;
import se.ki.education.nkcx.service.general.PersonCellService;

import java.util.List;

@RestController
@RequestMapping("/api/person_cell")
@Api(tags = "PersonCell", description = "APIs related to PersonCell.")
public class PersonCellController {

    @Autowired
    private PersonCellService personCellService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonCellRes> getPersonCell(@PathVariable Long id) {
        PersonCellRes personCellRes = personCellService.getById(id);
        return ResponseEntity.ok(personCellRes);
    }

    @GetMapping
    public ResponseEntity<List<PersonCellRes>> getAllPersonCell() {
        List<PersonCellRes> personCellResList = personCellService.getAll();
        return ResponseEntity.ok(personCellResList);
    }

    @GetMapping("/filter")
    @ApiOperation(value = "Filter based on person.")
    public ResponseEntity<List<PersonCellRes>> filterByPerson(
            @RequestParam(required = false) List<Long> personId) {

        // Call the service method
        List<PersonCellRes> personCellRes = personCellService.filterByPerson(personId);

        return ResponseEntity.ok(personCellRes);
    }

    @GetMapping("/search")
    @ApiOperation("Search for persons based on Cell.")
    public ResponseEntity<List<PersonCellRes>> searchByCellName(@RequestParam String name) {
        List<PersonCellRes> personCellRes = personCellService.searchByCellName(name);
        return personCellRes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(personCellRes);
    }
}
