package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.CellReq;
import se.ki.education.nkcx.dto.request.PersonCellReq;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.entity.*;

@Component
public class PersonCellDtoUtil implements DtoUtil<PersonCellEntity, PersonCellReq, PersonCellRes>{

    private final DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil;
    private final DtoUtil<CellEntity, CellReq, CellRes> cellDtoUtil;

    public PersonCellDtoUtil(DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil, DtoUtil<CellEntity, CellReq, CellRes> cellDtoUtil) {
        this.personDtoUtil = personDtoUtil;
        this.cellDtoUtil = cellDtoUtil;
    }

    @Override
    public PersonCellEntity reqToEntity(PersonCellReq personCellReq) {
        if (personCellReq == null) {
            return null;
        }
        PersonCellEntity personCellEntity = new PersonCellEntity();

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personCellReq.getPersonId());
        personCellEntity.setPersonEntity(personEntity);

        CellEntity cellEntity = new CellEntity();
        cellEntity.setId(personCellReq.getCellId());
        personCellEntity.setCellEntity(cellEntity);

        return personCellEntity;
    }

    @Override
    public PersonCellRes entityToRes(PersonCellEntity personCellEntity) {
        if (personCellEntity == null){
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personCellEntity.getPersonEntity());
        CellRes cellRes = cellDtoUtil.entityToRes(personCellEntity.getCellEntity());
        return new PersonCellRes(personCellEntity.getId(), cellRes, personRes);
    }

    @Override
    public PersonCellRes prepRes(PersonCellEntity personCellEntity) {
        if (personCellEntity == null) {
            return null;
        }
        PersonRes personRes = personDtoUtil.entityToRes(personCellEntity.getPersonEntity());
        CellRes cellRes = cellDtoUtil.prepRes(personCellEntity.getCellEntity());

        return new PersonCellRes(personCellEntity.getId(), cellRes, personRes);
    }

    @Override
    public void setUpdatedValue(PersonCellReq personCellReq, PersonCellEntity personCellEntity) {
        if (personCellReq == null || personCellEntity == null) {
            return;
        }

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(personCellReq.getPersonId());
        personCellEntity.setPersonEntity(personEntity);

        CellEntity cellEntity = new CellEntity();
        cellEntity.setId(personCellReq.getCellId());
        personCellEntity.setCellEntity(cellEntity);

    }
}
