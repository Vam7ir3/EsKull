package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.CountyReq;
import se.ki.education.nkcx.dto.response.CountyRes;
import se.ki.education.nkcx.entity.CountyEntity;

@Component
public class CountyDtoUtil implements DtoUtil<CountyEntity, CountyReq, CountyRes>{
    @Override
    public CountyEntity reqToEntity(CountyReq countyReq) {
        return new CountyEntity()
                .setName(countyReq.getName());
    }

    @Override
    public CountyRes entityToRes(CountyEntity countyEntity) {
        return new CountyRes()
                .setId(countyEntity.getId())
                .setName(countyEntity.getName());
    }

    @Override
    public CountyRes prepRes(CountyEntity countyEntity) {
        return entityToRes(countyEntity);
    }

    @Override
    public void setUpdatedValue(CountyReq countyReq, CountyEntity countyEntity) {
        if (countyReq != null && countyEntity != null) {
            if (countyReq.getName() != null && !countyReq.getName().equals(countyEntity.getName())) {
                countyEntity.setName(countyReq.getName());
            }
        }
    }
}
