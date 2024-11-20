package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;

import java.util.List;

public interface PersonSampleService {
    PaginationRes<PersonSampleRes> get(PaginationReq paginationReq);
    PersonSampleRes getById(Long id);

    List<PersonSampleRes> getAll();

    List<PersonSampleRes> filterByPerson(List<Long> personIds);

    List<PersonSampleRes> searchBySampleName(String type);
}


