package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonHpvRes;
import se.ki.education.nkcx.dto.response.PersonRes;

import java.util.List;

public interface PersonHpvService {
    PersonHpvRes getById(Long id);

    PaginationRes<PersonHpvRes> get(PaginationReq paginationReq);

    List<PersonHpvRes> getAll();

    List<PersonHpvRes> filterByPerson(List<Long> personIds);

    List<PersonHpvRes> searchByHpvName(String name);



}


