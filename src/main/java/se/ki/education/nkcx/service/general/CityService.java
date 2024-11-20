package se.ki.education.nkcx.service.general;


import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.CityReq;
import se.ki.education.nkcx.dto.response.CityRes;

public interface CityService {
    CityRes save(CityReq cityReq);

    PaginationRes<CityRes> get(PaginationReq paginationReq);

    PaginationRes<CityRes> getByState(Long stateId, PaginationReq paginationReq);

    CityRes update(CityReq cityReq);

    void delete(Long id);
}
