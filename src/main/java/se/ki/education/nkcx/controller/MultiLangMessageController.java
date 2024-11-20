package se.ki.education.nkcx.controller;

import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.MultiLangMessageReq;
import se.ki.education.nkcx.dto.response.MultiLangMessageRes;
import se.ki.education.nkcx.service.MultiLangMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/multi_lang_message")
@Api(tags = "Multi-Language Message")
public class MultiLangMessageController {

    private final MultiLangMessageService multiLangMessageService;
    private final ServiceResponseUtil<MultiLangMessageRes> multiLangMessageResServiceResponseUtil;

    @Autowired
    public MultiLangMessageController(MultiLangMessageService multiLangMessageService, ServiceResponseUtil<MultiLangMessageRes> multiLangMessageResServiceResponseUtil) {
        this.multiLangMessageService = multiLangMessageService;
        this.multiLangMessageResServiceResponseUtil = multiLangMessageResServiceResponseUtil;
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all MultiLangMessage list. If pagination detail is not provided then all the MultiLangMessage will be fetched.")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {

        return ResponseEntity.ok(multiLangMessageResServiceResponseUtil.buildServiceResponse(
                true,
                "MultiLangMessages retrieved",
                multiLangMessageService.get(pagination)
                )
        );
    }

    @PutMapping
    @ApiOperation(value = "Update an existing Info.")
    public ResponseEntity<ServiceRes> update(@RequestBody MultiLangMessageReq multiLangMessage) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("MultiLangMessage updated.")
                        .addData("multiLangMessage", multiLangMessageService.update(multiLangMessage))
        );
    }
}
