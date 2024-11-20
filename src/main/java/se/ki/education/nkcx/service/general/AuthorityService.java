package se.ki.education.nkcx.service.general;


import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.AuthorityRes;

public interface AuthorityService {

    PaginationRes<AuthorityRes> get(PaginationReq paginationReq);
}
