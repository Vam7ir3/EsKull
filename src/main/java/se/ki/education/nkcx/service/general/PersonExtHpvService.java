package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.response.PersonExtHpvRes;

import java.util.List;

public interface PersonExtHpvService {

    PersonExtHpvRes getById(Long id);

    List<PersonExtHpvRes> getAll();

    List<PersonExtHpvRes> filterByPerson(List<Long> personIds);

    List<PersonExtHpvRes> searchByExtHpvName(String name);
}
