package se.ki.education.nkcx.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.PersonHpvReq;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonHpvRes;
import se.ki.education.nkcx.dto.response.HpvRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.util.PersonHpvDtoUtil;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.PersonHpvEntity;
import se.ki.education.nkcx.entity.HpvEntity;
import se.ki.education.nkcx.repo.PersonHpvRepo;
import se.ki.education.nkcx.repo.HpvRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonHpvServiceImpl implements PersonHpvService {

    private final PersonHpvRepo personHpvRepo;

    private final HpvRepo hpvRepo;

    private LogService logService;

    private final DtoUtil<PersonHpvEntity, PersonHpvReq, PersonHpvRes> dtoUtil;

    private final PaginationDtoUtil<PersonHpvEntity, PersonHpvReq, PersonHpvRes> paginationDtoUtil;

    public PersonHpvServiceImpl(PersonHpvRepo personHpvRepo, HpvRepo hpvRepo, DtoUtil<PersonHpvEntity, PersonHpvReq, PersonHpvRes> dtoUtil, PaginationDtoUtil<PersonHpvEntity, PersonHpvReq, PersonHpvRes> paginationDtoUtil) {
        this.personHpvRepo = personHpvRepo;
        this.hpvRepo = hpvRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @Override
    public PersonHpvRes getById(Long id) {
        PersonHpvEntity personHpvEntity = personHpvRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonHpv not found"));
        return dtoUtil.entityToRes(personHpvEntity);
    }

    @Override
    public PaginationRes<PersonHpvRes> get(PaginationReq paginationReq) {
        List<String> validFields = Arrays.asList("createdDate");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            sortBy = "createdDate";
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<PersonHpvEntity> personHpvEntityPage = personHpvRepo.findAll(pageable);

        if ("name".equals(sortBy)) {
            personHpvEntityPage = personHpvRepo.findAll(pageable);
        } else {
            personHpvEntityPage = personHpvRepo.findAll(pageable);
        }
        List<PersonHpvRes> personHpvRes = personHpvEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(personHpvEntityPage, personHpvRes);
    }

    @Override
    public List<PersonHpvRes> getAll() {
        List<PersonHpvEntity> personHpvEntities = personHpvRepo.findAll();
        return personHpvEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    public List<PersonHpvRes> filterByPerson(List<Long> personIds) {
        if (personIds == null || personIds.isEmpty()) {
            return getAll(); // Return all if no filters are provided
        }

        List<PersonHpvRes> result = new ArrayList<>();
        for (Long personId : personIds) {
            List<PersonHpvEntity> entities = personHpvRepo.findByPersonEntityId(personId);
            result.addAll(entities.stream()
                    .map(dtoUtil::prepRes)
                    .collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public List<PersonHpvRes> searchByHpvName(String name) {
        Optional<HpvEntity> hpvEntityOpt = hpvRepo.findByName(name);


        HpvEntity hpvEntity = hpvEntityOpt.get();
        // Find PersonHpvEntities by hpv ID
        List<PersonHpvEntity> personHpvEntities = personHpvRepo.findByHpvEntityIdIn(Collections.singletonList(hpvEntity.getId()));

        // Convert to PersonHpvRes
        return personHpvEntities.stream()
                .map(entity -> {
                    PersonRes personRes = new PersonRes()
                            .setId(entity.getPersonEntity().getId())
                            .setPnr(entity.getPersonEntity().getPnr());


                    HpvRes hpvRes = new HpvRes()
                            .setId(entity.getHpvEntity().getId())
                            .setName(entity.getHpvEntity().getName());

                    return new PersonHpvRes(entity.getId(), hpvRes, personRes);
                })
                .collect(Collectors.toList());
    }
}