package se.ki.education.nkcx.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.dto.util.PersonExtHpvDtoUtil;
import se.ki.education.nkcx.entity.ExtHpvEntity;
import se.ki.education.nkcx.entity.PersonExtHpvEntity;
import se.ki.education.nkcx.repo.ExtHpvRepo;
import se.ki.education.nkcx.repo.PersonExtHpvRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonExtHpvServiceImpl implements PersonExtHpvService{

    @Autowired
    private PersonExtHpvRepo personExtHpvRepo;

    @Autowired
    private ExtHpvRepo extHpvRepo;

    @Autowired
    private PersonExtHpvDtoUtil dtoUtil;

    @Override
    public PersonExtHpvRes getById(Long id) {
        PersonExtHpvEntity personExtHpvEntity = personExtHpvRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonExtHpv not found"));
        return dtoUtil.entityToRes(personExtHpvEntity);
    }

    @Override
    public List<PersonExtHpvRes> getAll() {
        List<PersonExtHpvEntity> personExtHpvEntities = personExtHpvRepo.findAll();
        return personExtHpvEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonExtHpvRes> filterByPerson(List<Long> personIds) {
        if (personIds == null || personIds.isEmpty()) {
            return getAll(); // Return all if no filters are provided
        }

        List<PersonExtHpvRes> result = new ArrayList<>();
        for (Long personId : personIds) {
            List<PersonExtHpvEntity> entities = personExtHpvRepo.findByPersonEntityId(personId);
            result.addAll(entities.stream()
                    .map(dtoUtil::prepRes)
                    .collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public List<PersonExtHpvRes> searchByExtHpvName(String name) {
        Optional<ExtHpvEntity> extHpvEntityOpt = extHpvRepo.findByName(name);


        ExtHpvEntity extHpvEntity = extHpvEntityOpt.get();
        // Find PersonExtHpvEntities by hpv ID
        List<PersonExtHpvEntity> personExtHpvEntities = personExtHpvRepo.findByExtHpvEntityIdIn(Collections.singletonList(extHpvEntity.getId()));

        // Convert to PersonHpvRes
        return personExtHpvEntities.stream()
                .map(entity -> {
                    PersonRes personRes = new PersonRes()
                            .setId(entity.getPersonEntity().getId())
                            .setPnr(entity.getPersonEntity().getPnr());


                    ExtHpvRes extHpvRes = new ExtHpvRes()
                            .setId(entity.getExtHpvEntity().getId())
                            .setName(entity.getExtHpvEntity().getName());

                    return new PersonExtHpvRes(entity.getId(), extHpvRes, personRes);
                })
                .collect(Collectors.toList());
    }
}
