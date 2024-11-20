package se.ki.education.nkcx.controller.country;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.CountryReq;
import se.ki.education.nkcx.service.general.CountryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/country")
@Api(tags = "Country", description = "APIs related to Country.")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Country.")
    public ResponseEntity<ServiceRes> save(@RequestBody CountryReq country) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Country saved.")
                        .addData("country", countryService.save(country))
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Country.")
    public ResponseEntity<ServiceRes> update(@RequestBody CountryReq country) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Country updated.")
                        .addData("country", countryService.update(country))
        );
    }

    @DeleteMapping(value = "/{countryId}")
    @ApiOperation(value = "Delete an existing Country.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long countryId) {
        countryService.delete(countryId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Country deleted.")
        );
    }
}
