package se.ki.education.nkcx.dto;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.response.CustomPaginationSessionRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.ServiceRes;

@Component
public class ServiceResponseUtil<Res> {
    public ServiceRes buildServiceResponse(boolean status, String message, PaginationRes<Res> paginationRes) {
        return new ServiceRes()
                .setStatus(status)
                .setMessage(message)
                .setStartPosition(paginationRes.getStartPosition())
                .setEndPosition(paginationRes.getEndPosition())
                .setTotalRecord(paginationRes.getTotalRecord())
                .setTotalPage(paginationRes.getTotalPage())
                .setPageSize(paginationRes.getPageSize())
                .setCurrentPage(paginationRes.getCurrentPage())
                .addData("list", paginationRes.getData());
    }

    public ServiceRes buildCustomServiceResponse(boolean status, String message, CustomPaginationSessionRes<Res> paginationResDto) {
        return new ServiceRes()
                .setStatus(status)
                .setMessage(message)
                .setStartPosition(paginationResDto.getStartPosition())
                .setEndPosition(paginationResDto.getEndPosition())
                .setTotalRecord(paginationResDto.getTotalRecord())
                .setTotalPage(paginationResDto.getTotalPage())
                .setPageSize(paginationResDto.getPageSize())
                .setCurrentPage(paginationResDto.getCurrentPage())
                .addData("list", paginationResDto.getData());
    }
}
