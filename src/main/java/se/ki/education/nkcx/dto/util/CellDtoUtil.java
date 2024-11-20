package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.CellReq;
import se.ki.education.nkcx.dto.response.CellRes;
import se.ki.education.nkcx.entity.CellEntity;

@Component
public class CellDtoUtil implements DtoUtil<CellEntity, CellReq, CellRes>{

    @Override
    public CellEntity reqToEntity(CellReq cellReq) {
        return new CellEntity()
                .setName(cellReq.getName());
    }

    @Override
    public CellRes entityToRes(CellEntity cellEntity) {
        return new CellRes()
                .setId(cellEntity.getId())
                .setName(cellEntity.getName());
    }

    @Override
    public CellRes prepRes(CellEntity cellEntity) {
        return entityToRes(cellEntity);
    }

    @Override
    public void setUpdatedValue(CellReq cellReq, CellEntity cellEntity) {
        if (cellReq != null && cellEntity != null) {
            if (cellReq.getName() != null && !cellReq.getName().equals(cellEntity.getName())) {
                cellEntity.setName(cellReq.getName());
            }
        }

    }
}
