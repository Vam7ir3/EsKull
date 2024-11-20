package se.ki.education.nkcx.controller.user;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.PasswordReq;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.UserReq;
import se.ki.education.nkcx.dto.response.UserResDto;
import se.ki.education.nkcx.repo.projection.UserProjection;
import se.ki.education.nkcx.service.user.UserService;
import se.ki.education.nkcx.service.user.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
@Api(tags = "User")
public class UserController {

    private final UserService userService;
    private final ServiceResponseUtil<UserResDto> serviceResponseUtil;
    private final ServiceResponseUtil<UserProjection> userProjectionServiceResponseUtil;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, ServiceResponseUtil<UserResDto> serviceResponseUtil, ServiceResponseUtil<UserProjection> userProjectionServiceResponseUtil) {
        this.userService = userServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
        this.userProjectionServiceResponseUtil = userProjectionServiceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new User.")
    public ResponseEntity<ServiceRes> signUp(@RequestBody UserReq user) throws IOException {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("User saved.")
                        .addData("user", userService.save(user))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all User list. If pagination detail is not provided then all the users will be fetched..")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                        true,
                        "Users retrieved.",
                        userService.get(pagination)
                )
        );
    }

    @GetMapping(value = "/by_role/{roleId}")
    @ApiOperation(value = "Get paginated list or all User list by Role. If pagination detail is not provided then all the data will be fetched..")
    public ResponseEntity<ServiceRes> getByRole(PaginationReq pagination, @PathVariable Long roleId) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                        true,
                        "Users retrieved.",
                        userService.getByRole(pagination, roleId)
                )
        );
    }

    @GetMapping(value = "/by_role_title")
    @ApiOperation(value = "Get paginated list or all User list by Role Titles. If pagination detail is not provided then all the data will be fetched..")
    public ResponseEntity<ServiceRes> getByRoleTitles(PaginationReq pagination,
                                                      @RequestParam List<String> roleTitles) {
        return ResponseEntity.ok(userProjectionServiceResponseUtil.buildServiceResponse(
                        true,
                        "Users retrieved.",
                        userService.getByRoleTitles(pagination, roleTitles)
                )
        );
    }

    @GetMapping(value = "/profile")
    @ApiOperation(value = "Get logged in User detail.")
    public ResponseEntity<ServiceRes> getUser() {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("User retrieved.")
                        .addData("user", userService.get())
        );
    }

    @GetMapping(value = "/fields")
    @ApiOperation(value = "Get paginated list or all User list with limited fields. If pagination detail is not provided then all the users will be fetched..")
    public ResponseEntity<ServiceRes> getLimited(PaginationReq pagination,
                                                 @RequestParam(required = false) List<String> fields) {

        return ResponseEntity.ok(userProjectionServiceResponseUtil.buildServiceResponse(
                        true,
                        "Users retrieved.",
                        userService.getLimited(fields, pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing User.")
    public ResponseEntity<ServiceRes> update(@RequestBody UserReq user) throws IOException {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("User updated.")
                        .addData("user", userService.update(user))
        );
    }

    @PutMapping(value = "/change_password")
    @ApiOperation(value = "Change password.")
    public ResponseEntity<ServiceRes> changePassword(@RequestBody PasswordReq password) {
        userService.changePassword(password);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Password changed.")
        );
    }

    @DeleteMapping(value = "/{userId}")
    @ApiOperation(value = "Permanently delete an existing User and his/her related data along with.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("User deleted permanently.")
        );
    }

    @PutMapping(value = "/logout")
    @ApiOperation(value = "Logout User from Device by marking as loggedout")
    public ResponseEntity<ServiceRes> logout() {
        userService.logout();
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Logged out.")
        );
    }
}
