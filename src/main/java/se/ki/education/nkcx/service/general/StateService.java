package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.StateReq;
import se.ki.education.nkcx.dto.response.StateResDto;

public interface StateService {
    StateResDto save(StateReq stateReq);

    PaginationRes<StateResDto> get(PaginationReq paginationReq);

    PaginationRes<StateResDto> getByCountry(Long countryId, PaginationReq paginationReq);

    StateResDto update(StateReq stateReq);

    void delete(Long id);
}
