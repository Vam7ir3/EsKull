package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.HpvReq;
import se.ki.education.nkcx.dto.response.HpvRes;
import se.ki.education.nkcx.entity.HpvEntity;

@Component
public class HpvDtoUtil implements DtoUtil<HpvEntity, HpvReq, HpvRes>{

    @Override
    public HpvEntity reqToEntity(HpvReq hpvReq) {
        return new HpvEntity()
                .setName(hpvReq.getName());
    }

    @Override
    public HpvRes entityToRes(HpvEntity hpvEntity) {
        return new HpvRes()
                .setId(hpvEntity.getId())
                .setName(hpvEntity.getName());
    }

    @Override
    public HpvRes prepRes(HpvEntity hpvEntity) {
        return entityToRes(hpvEntity);
    }

    @Override
    public void setUpdatedValue(HpvReq hpvReq, HpvEntity hpvEntity) {
        if (hpvReq != null && hpvEntity != null) {
            if (hpvReq.getName() != null && !hpvReq.getName().equals(hpvEntity.getName())) {
                hpvEntity.setName(hpvReq.getName());
            }
        }

    }
}
