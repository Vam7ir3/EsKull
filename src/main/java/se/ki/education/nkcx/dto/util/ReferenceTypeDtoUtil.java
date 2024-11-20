package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.ReferenceTypeReq;
import se.ki.education.nkcx.dto.response.ReferenceTypeRes;
import se.ki.education.nkcx.entity.ReferenceTypeEntity;

@Component
public class ReferenceTypeDtoUtil implements DtoUtil<ReferenceTypeEntity, ReferenceTypeReq, ReferenceTypeRes> {
    @Override
    public ReferenceTypeEntity reqToEntity(ReferenceTypeReq referenceTypeReq) {
        return new ReferenceTypeEntity()
                .setType(referenceTypeReq.getType());
    }

    @Override
    public ReferenceTypeRes entityToRes(ReferenceTypeEntity referenceTypeEntity) {
        return new ReferenceTypeRes()
                .setId(referenceTypeEntity.getId())
                .setType(referenceTypeEntity.getType());
    }

    @Override
    public ReferenceTypeRes prepRes(ReferenceTypeEntity referenceTypeEntity) {
        return entityToRes(referenceTypeEntity);
    }

    @Override
    public void setUpdatedValue(ReferenceTypeReq referenceTypeReq, ReferenceTypeEntity referenceTypeEntity) {
        if(referenceTypeReq != null && referenceTypeEntity != null) {
            if(referenceTypeReq.getType() != null && !referenceTypeReq.getType().equals(referenceTypeEntity.getType())) {
                referenceTypeEntity.setType(referenceTypeReq.getType());
            }
        }
    }
}
