package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.ExtHpvReq;
import se.ki.education.nkcx.dto.request.PersonExtHpvReq;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.entity.*;

@Component
public class PersonExtHpvDtoUtil implements DtoUtil<PersonExtHpvEntity, PersonExtHpvReq, PersonExtHpvRes>{

    private final DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil;
    private final DtoUtil<ExtHpvEntity, ExtHpvReq, ExtHpvRes> extHpvDtoUtil;

    public PersonExtHpvDtoUtil(DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil, DtoUtil<ExtHpvEntity, ExtHpvReq, ExtHpvRes> extHpvDtoUtil) {
        this.personDtoUtil = personDtoUtil;
        this.extHpvDtoUtil = extHpvDtoUtil;
    }

    @Override
    public PersonExtHpvEntity reqToEntity(PersonExtHpvReq personExtHpvReq) {
        if (personExtHpvReq == null) {
            return null;
        }
        PersonExtHpvEntity personExtHpvEntity = new PersonExtHpvEntity();

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personExtHpvReq.getPersonId());
        personExtHpvEntity.setPersonEntity(personEntity);

        ExtHpvEntity extHpvEntity = new ExtHpvEntity();
        extHpvEntity.setId(personExtHpvReq.getExtHpvId());
        personExtHpvEntity.setExtHpvEntity(extHpvEntity);

        return personExtHpvEntity;
    }

    @Override
    public PersonExtHpvRes entityToRes(PersonExtHpvEntity personExtHpvEntity) {
        if (personExtHpvEntity == null){
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personExtHpvEntity.getPersonEntity());
        ExtHpvRes extHpvRes = extHpvDtoUtil.entityToRes(personExtHpvEntity.getExtHpvEntity());
        return new PersonExtHpvRes(personExtHpvEntity.getId(), extHpvRes, personRes);
    }

    @Override
    public PersonExtHpvRes prepRes(PersonExtHpvEntity personExtHpvEntity) {
        if (personExtHpvEntity == null) {
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personExtHpvEntity.getPersonEntity());
        ExtHpvRes extHpvRes = extHpvDtoUtil.prepRes(personExtHpvEntity.getExtHpvEntity());

        return new PersonExtHpvRes(personExtHpvEntity.getId(), extHpvRes, personRes);
    }

    @Override
    public void setUpdatedValue(PersonExtHpvReq personExtHpvReq, PersonExtHpvEntity personExtHpvEntity) {
        if (personExtHpvReq == null || personExtHpvEntity == null) {
            return;
        }

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personExtHpvReq.getPersonId());
        personExtHpvEntity.setPersonEntity(personEntity);

        ExtHpvEntity extHpvEntity = new ExtHpvEntity();
        extHpvEntity.setId(personExtHpvReq.getExtHpvId());
        personExtHpvEntity.setExtHpvEntity(extHpvEntity);

    }
}
