package se.ki.education.nkcx.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonCellRes;
import se.ki.education.nkcx.dto.response.CellRes;
import se.ki.education.nkcx.dto.util.PersonCellDtoUtil;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.PersonCellEntity;
import se.ki.education.nkcx.entity.CellEntity;
import se.ki.education.nkcx.repo.PersonCellRepo;
import se.ki.education.nkcx.repo.CellRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class PersonCellServiceImpl implements PersonCellService {

    @Autowired
    private PersonCellRepo personCellRepo;

    @Autowired
    private CellRepo cellRepo;

    @Autowired
    private PersonCellDtoUtil dtoUtil;

    @Override
    public PersonCellRes getById(Long id) {
        PersonCellEntity personCellEntity = personCellRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonCell not found"));
        return dtoUtil.entityToRes(personCellEntity);
    }

    @Override
    public List<PersonCellRes> getAll() {
        List<PersonCellEntity> personCellEntities = personCellRepo.findAll();
        return personCellEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    public List<PersonCellRes> filterByPerson(List<Long> personIds) {
        if (personIds == null || personIds.isEmpty()) {
            return getAll(); // Return all if no filters are provided
        }

        List<PersonCellRes> result = new ArrayList<>();
        for (Long personId : personIds) {
            List<PersonCellEntity> entities = personCellRepo.findByPersonEntityId(personId);
            result.addAll(entities.stream()
                    .map(dtoUtil::prepRes)
                    .collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public List<PersonCellRes> searchByCellName(String name) {
        Optional<CellEntity> cellEntityOpt = cellRepo.findByName(name);


        CellEntity cellEntity = cellEntityOpt.get();
        // Find PersonCellEntities by Cell ID
        List<PersonCellEntity> personCellEntities = personCellRepo.findByCellEntityIdIn(Collections.singletonList(cellEntity.getId()));

        // Convert to PersonCellRes
        return personCellEntities.stream()
                .map(entity -> {
                    PersonRes personRes = new PersonRes()
                            .setId(entity.getPersonEntity().getId())
                            .setPnr(entity.getPersonEntity().getPnr());

                    CellRes cellRes = new CellRes()
                            .setId(entity.getCellEntity().getId())
                            .setName(entity.getCellEntity().getName());

                    return new PersonCellRes(entity.getId(), cellRes, personRes);
                })
                .collect(Collectors.toList());
    }



}