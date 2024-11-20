package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.RoleReq;
import se.ki.education.nkcx.dto.response.RoleResDto;

public interface RoleService {
    RoleResDto save(RoleReq roleReq);

    PaginationRes<RoleResDto> get(PaginationReq paginationReq);

    RoleResDto update(RoleReq roleReq);

    void delete(Long id);
}
