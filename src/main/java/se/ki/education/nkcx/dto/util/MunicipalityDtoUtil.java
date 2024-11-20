package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.MunicipalityReq;
import se.ki.education.nkcx.dto.response.MunicipalityRes;
import se.ki.education.nkcx.entity.MunicipalityEntity;

@Component
public class MunicipalityDtoUtil implements DtoUtil<MunicipalityEntity, MunicipalityReq, MunicipalityRes>{
    @Override
    public MunicipalityEntity reqToEntity(MunicipalityReq municipalityReq) {
        MunicipalityEntity municipalityEntity = new MunicipalityEntity();
        municipalityEntity.setName(municipalityReq.getName());
        municipalityEntity.setYear(municipalityReq.getYear());
        return municipalityEntity;
    }

    @Override
    public MunicipalityRes entityToRes(MunicipalityEntity municipalityEntity) {
        return new MunicipalityRes()
                .setId(municipalityEntity.getId())
                .setName(municipalityEntity.getName())
                .setYear(municipalityEntity.getYear());
    }

    @Override
    public MunicipalityRes prepRes(MunicipalityEntity municipalityEntity) {
        return entityToRes(municipalityEntity);
    }

    @Override
    public void setUpdatedValue(MunicipalityReq municipalityReq, MunicipalityEntity municipalityEntity) {
        if (municipalityReq != null && municipalityEntity != null) {
            if (municipalityReq.getName() != null && !municipalityReq.getName().equals(municipalityEntity.getName())) {
                municipalityEntity.setName(municipalityReq.getName());
            }
            if (municipalityReq.getYear() != null && !municipalityReq.getYear().equals(municipalityEntity.getYear())) {
                municipalityEntity.setYear(municipalityReq.getYear());
            }
        }
    }
}
