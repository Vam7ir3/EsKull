package se.ki.education.nkcx.controller.role;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.RoleReq;
import se.ki.education.nkcx.dto.response.RoleResDto;
import se.ki.education.nkcx.service.general.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/role")
@Api(tags = "Role", description = "APIs related to Role.")
public class RoleController {

    private final RoleService roleService;
    private final ServiceResponseUtil<RoleResDto> serviceResponseUtil;

    @Autowired
    public RoleController(RoleService roleService, ServiceResponseUtil<RoleResDto> serviceResponseUtil) {
        this.roleService = roleService;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Role.")
    public ResponseEntity<ServiceRes> save(@RequestBody RoleReq role) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Role saved.")
                        .addData("role", roleService.save(role))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Role list. If pagination detail is not provided then all the roles will be fetched..")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                true,
                "Roles retrieved.",
                roleService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Role.")
    public ResponseEntity<ServiceRes> update(@RequestBody RoleReq role) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Role updated.")
                        .addData("role", roleService.update(role))
        );
    }

    @DeleteMapping(value = "/{roleId}")
    @ApiOperation(value = "Delete an existing Role.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long roleId) {
        roleService.delete(roleId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Role deleted.")
        );
    }
}
