package se.ki.education.nkcx.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.PersonSampleReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;
import se.ki.education.nkcx.dto.response.SampleRes;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.util.PersonSampleDtoUtil;
import se.ki.education.nkcx.entity.PersonSampleEntity;
import se.ki.education.nkcx.entity.SampleEntity;
import se.ki.education.nkcx.repo.PersonSampleRepo;
import se.ki.education.nkcx.repo.SampleRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonSampleServiceImpl implements PersonSampleService {

    @Autowired
    private PersonSampleRepo personSampleRepo;

    @Autowired
    private SampleRepo sampleRepo;

    @Autowired
    private PersonSampleDtoUtil dtoUtil;

    private final PaginationDtoUtil<PersonSampleEntity, PersonSampleReq, PersonSampleRes> paginationDtoUtil;

    public PersonSampleServiceImpl(PaginationDtoUtil<PersonSampleEntity, PersonSampleReq, PersonSampleRes> paginationDtoUtil) {
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @Override
    public PaginationRes<PersonSampleRes> get(PaginationReq paginationReq) {
        List<String> validFields = Arrays.asList("createdDate");
        String sortBy = paginationReq.getSortBy();

        if (!validFields.contains(sortBy)) {
            sortBy = "createdDate";
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<PersonSampleEntity> personSampleEntities = personSampleRepo.findAll(pageable);

        if ("type".equals(sortBy)) {
            personSampleEntities = personSampleRepo.findAll(pageable);
        } else {
            personSampleEntities = personSampleRepo.findAll(pageable);
        }
        List<PersonSampleRes> personSampleRes = personSampleEntities.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(personSampleEntities, personSampleRes);

    }

    @Override
    public PersonSampleRes getById(Long id) {
        PersonSampleEntity personSampleEntity = personSampleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("PersonSample not found"));
        return dtoUtil.entityToRes(personSampleEntity);
    }

    @Override
    public List<PersonSampleRes> getAll() {
        List<PersonSampleEntity> personSampleEntities = personSampleRepo.findAll();
        return personSampleEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    public List<PersonSampleRes> filterByPerson(List<Long> personIds) {
        if (personIds == null || personIds.isEmpty()) {
            return getAll(); // Return all if no filters are provided
        }

        List<PersonSampleRes> result = new ArrayList<>();
        for (Long personId : personIds) {
            List<PersonSampleEntity> entities = personSampleRepo.findByPersonEntityId(personId);
            result.addAll(entities.stream()
                    .map(dtoUtil::prepRes)
                    .collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public List<PersonSampleRes> searchBySampleName(String type) {
        Optional<SampleEntity> sampleEntityOpt = sampleRepo.findByType(type);


        SampleEntity sampleEntity = sampleEntityOpt.get();
        // Find PersonSampleEntities by sample ID
        List<PersonSampleEntity> personSampleEntities = personSampleRepo.findBySampleEntityIdIn(Collections.singletonList(sampleEntity.getId()));

        // Convert to PersonSampleRes
        return personSampleEntities.stream()
                .map(entity -> {
                    PersonRes personRes = new PersonRes()
                            .setId(entity.getPersonEntity().getId())
                            .setPnr(entity.getPersonEntity().getPnr());


                    SampleRes sampleRes = new SampleRes()
                            .setId(entity.getSampleEntity().getId())
                            .setType(entity.getSampleEntity().getType());

                    return new PersonSampleRes(entity.getId(), sampleRes, personRes);
                })
                .collect(Collectors.toList());
    }


}