package se.ki.education.nkcx.controller.cell6923;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.Cell6923Req;
import se.ki.education.nkcx.dto.request.DistrictReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.Cell6923Res;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.service.general.Cell6923Service;
import se.ki.education.nkcx.service.general.Cell6923ServiceImpl;

@RestController
@RequestMapping(value = "/api/cell6923")
@Api(tags = "Cell6923", description = "APIs related to cell6923.")
public class Cell6923Controller {

    private final Cell6923Service cell6923Service;
    private final ServiceResponseUtil<Cell6923Res> cell6923DtoServiceResponseUtil;
    private final Cell6923ServiceImpl cell6923ServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public Cell6923Controller(Cell6923Service cell6923Service, ServiceResponseUtil<Cell6923Res> cell6923DtoServiceResponseUtil, Cell6923ServiceImpl cell6923ServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.cell6923Service = cell6923Service;
        this.cell6923DtoServiceResponseUtil = cell6923DtoServiceResponseUtil;
        this.cell6923ServiceImpl = cell6923ServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Cell6923.")
    public ResponseEntity<ServiceRes> save(@RequestBody Cell6923Req cell6923Req) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Cell6923 saved.")
                        .addData("cell6923", cell6923Service.save(cell6923Req))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Cell6923")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(cell6923DtoServiceResponseUtil.buildServiceResponse(
                true,
                "Cell6923 retrieved",
                cell6923Service.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new Cell6923")
    public ResponseEntity<ServiceRes> update(@RequestBody Cell6923Req cell6923Req) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Cell6923 updated.")
                        .addData("cell6923", cell6923Service.update(cell6923Req))
        );
    }

    @DeleteMapping(value = "/{cell6923Id}")
    @ApiOperation(value = "Delete an existing Cell6923.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long cell6923Id) {
        cell6923Service.delete(cell6923Id);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Cell6923 Deleted")
        );
    }
}
