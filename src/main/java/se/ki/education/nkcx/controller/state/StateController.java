package se.ki.education.nkcx.controller.state;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.StateReq;
import se.ki.education.nkcx.service.general.StateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/state")
@Api(tags = "State", description = "APIs related to State.")
public class StateController {

    private final StateService stateService;

    @Autowired
    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @PostMapping
    @ApiOperation(value = "Save a new State.")
    public ResponseEntity<ServiceRes> save(@RequestBody StateReq state) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("State saved.")
                        .addData("state", stateService.save(state))
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing State.")
    public ResponseEntity<ServiceRes> update(@RequestBody StateReq state) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("State updated.")
                        .addData("state", stateService.update(state))
        );
    }

    @DeleteMapping(value = "/{stateId}")
    @ApiOperation(value = "Delete an existing State.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long stateId) {
        stateService.delete(stateId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("State deleted.")
        );
    }
}
