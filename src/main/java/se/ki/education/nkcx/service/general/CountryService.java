package se.ki.education.nkcx.service.general;


import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.CountryReq;
import se.ki.education.nkcx.dto.response.CountryRes;

public interface CountryService {
    CountryRes save(CountryReq countryReq);

    PaginationRes<CountryRes> get(PaginationReq paginationReq);

    CountryRes update(CountryReq countryReq);

    void delete(Long id);
}
