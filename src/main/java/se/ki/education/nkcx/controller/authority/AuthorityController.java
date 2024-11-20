package se.ki.education.nkcx.controller.authority;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.response.AuthorityRes;
import se.ki.education.nkcx.service.general.AuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/authority")
@Api(tags = "Authority", description = "APIs related to Authority.")
public class AuthorityController {

    private final AuthorityService authorityService;
    private final ServiceResponseUtil<AuthorityRes> serviceResponseUtil;

    @Autowired
    public AuthorityController(AuthorityService authorityService, ServiceResponseUtil<AuthorityRes> serviceResponseUtil) {
        this.authorityService = authorityService;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Authority list. If pagination detail is not provided then all the Authorities will be fetched..")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(serviceResponseUtil.buildServiceResponse(
                true,
                "Authorities retrieved.",
                authorityService.get(pagination)
                )
        );
    }
}
