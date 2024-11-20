package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.ExtHpvReq;
import se.ki.education.nkcx.dto.response.ExtHpvRes;
import se.ki.education.nkcx.entity.ExtHpvEntity;

@Component
public class ExtHpvDtoUtil implements DtoUtil<ExtHpvEntity, ExtHpvReq, ExtHpvRes>{
    @Override
    public ExtHpvEntity reqToEntity(ExtHpvReq extHpvReq) {
        return new ExtHpvEntity()
                .setName(extHpvReq.getName());
    }

    @Override
    public ExtHpvRes entityToRes(ExtHpvEntity extHpvEntity) {
        return new ExtHpvRes()
                .setId(extHpvEntity.getId())
                .setName(extHpvEntity.getName());
    }

    @Override
    public ExtHpvRes prepRes(ExtHpvEntity extHpvEntity) {
        return entityToRes(extHpvEntity);
    }

    @Override
    public void setUpdatedValue(ExtHpvReq extHpvReq, ExtHpvEntity extHpvEntity) {
        if (extHpvReq != null && extHpvEntity != null) {
            if (extHpvReq.getName() != null && !extHpvReq.getName().equals(extHpvEntity.getName())) {
                extHpvEntity.setName(extHpvReq.getName());
            }
        }
    }
}
