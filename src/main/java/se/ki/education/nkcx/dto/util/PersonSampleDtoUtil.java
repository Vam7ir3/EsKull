package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.request.PersonSampleReq;
import se.ki.education.nkcx.dto.request.SampleReq;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;
import se.ki.education.nkcx.dto.response.SampleRes;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.PersonSampleEntity;
import se.ki.education.nkcx.entity.SampleEntity;

@Component
public class PersonSampleDtoUtil implements DtoUtil<PersonSampleEntity, PersonSampleReq, PersonSampleRes>{


    private final DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil;
    private final DtoUtil<SampleEntity, SampleReq, SampleRes> sampleDtoUtil;

    public PersonSampleDtoUtil(DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil, DtoUtil<SampleEntity, SampleReq, SampleRes> sampleDtoUtil) {
        this.personDtoUtil = personDtoUtil;
        this.sampleDtoUtil = sampleDtoUtil;
    }


    @Override
    public PersonSampleEntity reqToEntity(PersonSampleReq personSampleReq) {
        if (personSampleReq == null) {
            return null;
        }
        PersonSampleEntity personSampleEntity = new PersonSampleEntity();

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personSampleReq.getPersonId());
        personSampleEntity.setPersonEntity(personEntity);

        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(personSampleReq.getSampleId());
        personSampleEntity.setSampleEntity(sampleEntity);

        return personSampleEntity;
    }

    @Override
    public PersonSampleRes entityToRes(PersonSampleEntity personSampleEntity) {
        if (personSampleEntity == null){
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personSampleEntity.getPersonEntity());
        SampleRes sampleRes = sampleDtoUtil.entityToRes(personSampleEntity.getSampleEntity());
        return new PersonSampleRes(personSampleEntity.getId(), sampleRes, personRes);
    }

    @Override
    public PersonSampleRes prepRes(PersonSampleEntity personSampleEntity) {
        if (personSampleEntity == null) {
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personSampleEntity.getPersonEntity());
        SampleRes sampleRes = sampleDtoUtil.prepRes(personSampleEntity.getSampleEntity());

        return new PersonSampleRes(personSampleEntity.getId(), sampleRes, personRes);
    }

    @Override
    public void setUpdatedValue(PersonSampleReq personSampleReq, PersonSampleEntity personSampleEntity) {
        if (personSampleReq == null || personSampleEntity == null) {
            return;
        }

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personSampleReq.getPersonId());
        personSampleEntity.setPersonEntity(personEntity);

        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(personSampleReq.getSampleId());
        personSampleEntity.setSampleEntity(sampleEntity);
    }
}
