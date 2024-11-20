package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.response.PersonCellRes;

import java.util.List;

public interface PersonCellService {
    PersonCellRes getById(Long id);

    List<PersonCellRes> getAll();

    List<PersonCellRes> filterByPerson(List<Long> personIds);

    List<PersonCellRes> searchByCellName(String name);



}


