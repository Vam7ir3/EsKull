package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.LabReq;
import se.ki.education.nkcx.dto.response.LabRes;
import se.ki.education.nkcx.entity.LabEntity;

@Component
public class LabDtoUtil implements DtoUtil<LabEntity, LabReq, LabRes>{
    @Override
    public LabEntity reqToEntity(LabReq labReq) {
        return new LabEntity()
                .setName(labReq.getName())
                .setIsInUse(labReq.getIsInUse());
    }

    @Override
    public LabRes entityToRes(LabEntity labEntity) {
        return new LabRes()
                .setId(labEntity.getId())
                .setName(labEntity.getName())
                .setIsInUse(labEntity.getIsInUse());
    }

    @Override
    public LabRes prepRes(LabEntity labEntity) {
        return entityToRes(labEntity);
    }

    @Override
    public void setUpdatedValue(LabReq labReq, LabEntity labEntity) {
        if (labReq != null && labEntity != null) {
            if (labReq.getName() != null && !labReq.getName().equals(labEntity.getName())) {
                labEntity.setName(labReq.getName());
            }
            if (labReq.getIsInUse() != null && !labReq.getIsInUse().equals(labEntity.getIsInUse())) {
                labEntity.setIsInUse(labReq.getIsInUse());
            }
        }
    }
}
