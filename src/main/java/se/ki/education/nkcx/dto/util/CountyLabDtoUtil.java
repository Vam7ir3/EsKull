package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.CountyLabReq;
import se.ki.education.nkcx.dto.request.CountyReq;
import se.ki.education.nkcx.dto.request.LabReq;
import se.ki.education.nkcx.dto.response.CountyLabRes;
import se.ki.education.nkcx.dto.response.CountyRes;
import se.ki.education.nkcx.dto.response.LabRes;
import se.ki.education.nkcx.entity.CountyEntity;
import se.ki.education.nkcx.entity.CountyLabEntity;
import se.ki.education.nkcx.entity.LabEntity;

@Component
public class CountyLabDtoUtil implements DtoUtil<CountyLabEntity, CountyLabReq, CountyLabRes> {

    private final DtoUtil<CountyEntity, CountyReq, CountyRes> countyDtoUtil;
    private final DtoUtil<LabEntity, LabReq, LabRes> labDtoUtil;

    public CountyLabDtoUtil(DtoUtil<CountyEntity, CountyReq, CountyRes> countyDtoUtil, DtoUtil<LabEntity, LabReq, LabRes> labDtoUtil) {
        this.countyDtoUtil = countyDtoUtil;
        this.labDtoUtil = labDtoUtil;
    }

    @Override
    public CountyLabEntity reqToEntity(CountyLabReq countyLabReq) {
        CountyLabEntity countyLabEntity = new CountyLabEntity();

        CountyEntity countyEntity = new CountyEntity();
        countyEntity.setId(countyLabReq.getCountyId());
        countyLabEntity.setCountyEntity(countyEntity);

        LabEntity labEntity = new LabEntity();
        labEntity.setId(countyLabReq.getLabId());
        countyLabEntity.setLabEntity(labEntity);

        return countyLabEntity;
    }

    @Override
    public CountyLabRes entityToRes(CountyLabEntity countyLabEntity) {
        CountyRes countyRes = countyDtoUtil.entityToRes(countyLabEntity.getCountyEntity());
        LabRes labRes = labDtoUtil.entityToRes(countyLabEntity.getLabEntity());

        return new CountyLabRes(countyLabEntity.getId(), countyRes, labRes);
    }

    @Override
    public CountyLabRes prepRes(CountyLabEntity countyLabEntity) {
        CountyRes countyRes = countyDtoUtil.prepRes(countyLabEntity.getCountyEntity());
        LabRes labRes = labDtoUtil.prepRes(countyLabEntity.getLabEntity());

        return new CountyLabRes(countyLabEntity.getId(), countyRes, labRes);
    }

    @Override
    public void setUpdatedValue(CountyLabReq countyLabReq, CountyLabEntity countyLabEntity) {

        CountyEntity countyEntity = new CountyEntity();
        countyEntity.setId(countyLabReq.getCountyId());
        countyLabEntity.setCountyEntity(countyEntity);

        LabEntity labEntity = new LabEntity();
        labEntity.setId(countyLabReq.getLabId());
        countyLabEntity.setLabEntity(labEntity);
    }
}
