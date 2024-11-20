package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.request.CountyLabReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CountyLabRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.util.List;

public interface CountyLabService {
    CountyLabRes getById(Long id);

    List<CountyLabRes> getAll();

    List<CountyLabRes> filterByCounty(List<Long> countyIds);

    List<CountyLabRes> searchByLabName(String name);

    CountyLabRes saveCountyLab(CountyLabReq countyLabReq);

//    PaginationRes<CountyLabRes> getByCounty(PaginationReq paginationReq, Long countyId);
}
