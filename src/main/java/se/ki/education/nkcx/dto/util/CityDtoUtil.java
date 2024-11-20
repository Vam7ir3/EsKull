package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.CityReq;
import se.ki.education.nkcx.dto.response.CityRes;
import se.ki.education.nkcx.entity.CityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityDtoUtil implements DtoUtil<CityEntity, CityReq, CityRes> {

    private final StateDtoUtil stateDtoUtil;

    @Autowired
    public CityDtoUtil(StateDtoUtil stateDtoUtil) {
        this.stateDtoUtil = stateDtoUtil;
    }

    @Override
    public CityEntity reqToEntity(CityReq cityReq) {
        return new CityEntity()
                .setName(cityReq.getName());
    }

    @Override
    public CityRes entityToRes(CityEntity cityEntity) {
        return new CityRes()
                .setId(cityEntity.getId())
                .setName(cityEntity.getName());
    }

    @Override
    public CityRes prepRes(CityEntity cityEntity) {
        return entityToRes(cityEntity)
                .setState(stateDtoUtil.prepRes(cityEntity.getState()));
    }

    @Override
    public void setUpdatedValue(CityReq cityReq, CityEntity cityEntity) {
        if (cityReq != null && cityEntity != null) {
            if (cityReq.getName() != null && !cityReq.getName().equals(cityEntity.getName())) {
                cityEntity.setName(cityReq.getName());
            }
        }
    }
}
