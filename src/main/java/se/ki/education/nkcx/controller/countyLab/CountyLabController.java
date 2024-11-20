package se.ki.education.nkcx.controller.countyLab;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.request.CountyLabReq;
import se.ki.education.nkcx.dto.response.CountyLabRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;
import se.ki.education.nkcx.service.general.CountyLabService;

import java.util.List;

@RestController
@RequestMapping("/api/county_lab")
@Api(tags = "County Lab", description = "APIs related to County Lab.")
public class CountyLabController {

    @Autowired
    private CountyLabService countyLabService;

    @GetMapping("/{id}")
    public ResponseEntity<CountyLabRes> getCountyLab(@PathVariable Long id) {
        CountyLabRes countyLabRes = countyLabService.getById(id);
        return ResponseEntity.ok(countyLabRes);
    }

    @GetMapping
    public ResponseEntity<List<CountyLabRes>> getAllCountyLab() {
        List<CountyLabRes> countyLabResList = countyLabService.getAll();
        return ResponseEntity.ok(countyLabResList);
    }

    @PostMapping
    public ResponseEntity<CountyLabRes> saveCountyLab(@RequestBody CountyLabReq countyLabReq) {
        CountyLabRes savedCountyLab = countyLabService.saveCountyLab(countyLabReq);
        return ResponseEntity.ok(savedCountyLab);
    }


}
