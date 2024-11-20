package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.LaboratoryReq;
import se.ki.education.nkcx.dto.response.LaboratoryRes;
import se.ki.education.nkcx.entity.LaboratoryEntity;

@Component
public class LaboratoryDtoUtil implements DtoUtil<LaboratoryEntity, LaboratoryReq, LaboratoryRes>{
    @Override
    public LaboratoryEntity reqToEntity(LaboratoryReq laboratoryReq) {
        return new LaboratoryEntity()
                .setName(laboratoryReq.getName())
                .setIsInUse(laboratoryReq.getIsInUse())
                .setSosLab(laboratoryReq.getSosLab())
                .setSosLabName(laboratoryReq.getSosLabName())
                .setSosLongName(laboratoryReq.getSosLongName())
                .setRegion(laboratoryReq.getRegion());
    }

    @Override
    public LaboratoryRes entityToRes(LaboratoryEntity laboratoryEntity) {
        return new LaboratoryRes()
                .setId(laboratoryEntity.getId())
                .setName(laboratoryEntity.getName())
                .setIsInUse(laboratoryEntity.getIsInUse())
                .setSosLab(laboratoryEntity.getSosLab())
                .setSosLabName(laboratoryEntity.getSosLabName())
                .setSosLongName(laboratoryEntity.getSosLongName())
                .setRegion(laboratoryEntity.getRegion());
    }

    @Override
    public LaboratoryRes prepRes(LaboratoryEntity laboratoryEntity) {
        return entityToRes(laboratoryEntity);
    }

    @Override
    public void setUpdatedValue(LaboratoryReq laboratoryReq, LaboratoryEntity laboratoryEntity) {
        if (laboratoryReq != null && laboratoryEntity != null) {
            if (laboratoryReq.getName() != null && !laboratoryReq.getName().equals(laboratoryEntity.getName())) {
                laboratoryEntity.setName(laboratoryReq.getName());
            }
            if (laboratoryReq.getIsInUse() != null && !laboratoryReq.getIsInUse().equals(laboratoryEntity.getIsInUse())) {
                laboratoryEntity.setIsInUse(laboratoryEntity.getIsInUse());
            }
            if (laboratoryReq.getSosLab() != null && !laboratoryReq.getSosLab().equals(laboratoryEntity.getSosLab())) {
                laboratoryEntity.setSosLab(laboratoryReq.getSosLab());
            }
            if (laboratoryReq.getSosLabName() != null && !laboratoryReq.getSosLabName().equals(laboratoryEntity.getSosLabName())) {
                laboratoryEntity.setSosLabName(laboratoryReq.getSosLabName());
            }
            if (laboratoryReq.getSosLongName() != null && !laboratoryReq.getSosLongName().equals(laboratoryEntity.getSosLongName())) {
                laboratoryEntity.setSosLongName(laboratoryReq.getSosLongName());
            }
            if (laboratoryReq.getRegion() != null && !laboratoryReq.getRegion().equals(laboratoryEntity.getRegion())) {
                laboratoryEntity.setRegion(laboratoryReq.getRegion());
            }
        }
    }
}
