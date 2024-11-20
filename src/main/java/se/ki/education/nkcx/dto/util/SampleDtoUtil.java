package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.SampleReq;
import se.ki.education.nkcx.dto.response.SampleRes;
import se.ki.education.nkcx.entity.SampleEntity;

@Component
public class SampleDtoUtil implements DtoUtil<SampleEntity, SampleReq, SampleRes>{
    @Override
    public SampleEntity reqToEntity(SampleReq sampleReq) {
        return new SampleEntity()
                .setType(sampleReq.getType());
    }

    @Override
    public SampleRes entityToRes(SampleEntity sampleEntity) {
        return new SampleRes()
                .setId(sampleEntity.getId())
                .setType(sampleEntity.getType());
    }

    @Override
    public SampleRes prepRes(SampleEntity sampleEntity) {

            return entityToRes(sampleEntity);
    }

    @Override
    public void setUpdatedValue(SampleReq sampleReq, SampleEntity sampleEntity) {
        if (sampleReq != null && sampleEntity != null) {
            if (sampleReq.getType() != null && !sampleReq.getType().equals(sampleEntity.getType())) {
                sampleEntity.setType(sampleReq.getType());
            }
        }
    }
}
