package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.request.PersonHpvReq;
import se.ki.education.nkcx.dto.request.HpvReq;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonHpvRes;
import se.ki.education.nkcx.dto.response.HpvRes;
import se.ki.education.nkcx.entity.PersonEntity;
import se.ki.education.nkcx.entity.PersonHpvEntity;
import se.ki.education.nkcx.entity.HpvEntity;

@Component
public class PersonHpvDtoUtil implements DtoUtil<PersonHpvEntity, PersonHpvReq, PersonHpvRes>{


    private final DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil;
    private final DtoUtil<HpvEntity, HpvReq, HpvRes> hpvDtoUtil;

    public PersonHpvDtoUtil(DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil, DtoUtil<HpvEntity, HpvReq, HpvRes> hpvDtoUtil) {
        this.personDtoUtil = personDtoUtil;
        this.hpvDtoUtil = hpvDtoUtil;
    }


    @Override
    public PersonHpvEntity reqToEntity(PersonHpvReq personHpvReq) {
        if (personHpvReq == null) {
            return null;
        }
        PersonHpvEntity personHpvEntity = new PersonHpvEntity();

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personHpvReq.getPersonId());
        personHpvEntity.setPersonEntity(personEntity);

        HpvEntity hpvEntity = new HpvEntity();
        hpvEntity.setId(personHpvReq.getHpvId());
        personHpvEntity.setHpvEntity(hpvEntity);

        return personHpvEntity;
    }

    @Override
    public PersonHpvRes entityToRes(PersonHpvEntity personHpvEntity) {
        if (personHpvEntity == null){
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personHpvEntity.getPersonEntity());
        HpvRes hpvRes = hpvDtoUtil.entityToRes(personHpvEntity.getHpvEntity());
        return new PersonHpvRes(personHpvEntity.getId(), hpvRes, personRes);
    }

    @Override
    public PersonHpvRes prepRes(PersonHpvEntity personHpvEntity) {
        if (personHpvEntity == null) {
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personHpvEntity.getPersonEntity());
        HpvRes hpvRes = hpvDtoUtil.prepRes(personHpvEntity.getHpvEntity());

        return new PersonHpvRes(personHpvEntity.getId(), hpvRes, personRes);
    }

    @Override
    public void setUpdatedValue(PersonHpvReq personHpvReq, PersonHpvEntity personHpvEntity) {
        if (personHpvReq == null || personHpvEntity == null) {
            return;
        }

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personHpvReq.getPersonId());
        personHpvEntity.setPersonEntity(personEntity);

        HpvEntity hpvEntity = new HpvEntity();
        hpvEntity.setId(personHpvReq.getHpvId());
        personHpvEntity.setHpvEntity(hpvEntity);
    }
}
