package se.ki.education.nkcx.controller.city;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.CityReq;
import se.ki.education.nkcx.service.general.CityService;
import se.ki.education.nkcx.service.general.CityServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/city")
@Api(tags = "City", description = "APIs related to City.")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityServiceImpl cityServiceImpl) {
        this.cityService = cityServiceImpl;
    }

    @PostMapping
    @ApiOperation(value = "Save a new City.")
    public ResponseEntity<ServiceRes> save(@RequestBody CityReq city) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("City saved.")
                        .addData("city", cityService.save(city))
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing City.")
    public ResponseEntity<ServiceRes> update(@RequestBody CityReq city) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("City updated.")
                        .addData("city", cityService.update(city))
        );
    }

    @DeleteMapping(value = "/{cityId}")
    @ApiOperation(value = "Delete an existing City.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long cityId) {
        cityService.delete(cityId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("City deleted.")
        );
    }
}
