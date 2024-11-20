package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.DistrictReq;
import se.ki.education.nkcx.dto.response.DistrictRes;
import se.ki.education.nkcx.entity.DistrictEntity;

@Component
public class DistrictDtoUtil implements DtoUtil<DistrictEntity, DistrictReq, DistrictRes> {
    @Override
    public DistrictEntity reqToEntity(DistrictReq districtReq) {
        return new DistrictEntity()
                .setDistrict(districtReq.getDistrict())
                .setDistrictName(districtReq.getDistrictName());
    }

    @Override
    public DistrictRes entityToRes(DistrictEntity districtEntity) {
        return new DistrictRes()
                .setId(districtEntity.getId())
                .setDistrict(districtEntity.getDistrict())
                .setDistrictName(districtEntity.getDistrictName());
    }

    @Override
    public DistrictRes prepRes(DistrictEntity districtEntity) {
        return entityToRes(districtEntity);
    }

    @Override
    public void setUpdatedValue(DistrictReq districtReq, DistrictEntity districtEntity) {
        if(districtReq != null && districtEntity != null) {
            if(districtReq.getDistrict() != null && !districtReq.getDistrict().equals(districtEntity.getDistrict())) {
                districtEntity.setDistrict(districtReq.getDistrict());
            }
            if(districtReq.getDistrictName() != null && !districtReq.getDistrictName().equals(districtEntity.getDistrictName())) {
                districtEntity.setDistrictName(districtReq.getDistrictName());
            }
        }
    }
}
