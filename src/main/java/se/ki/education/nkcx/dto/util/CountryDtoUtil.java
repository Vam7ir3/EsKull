package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.CountryReq;
import se.ki.education.nkcx.dto.response.CountryRes;
import se.ki.education.nkcx.entity.CountryEntity;
import org.springframework.stereotype.Component;

@Component
public class CountryDtoUtil implements DtoUtil<CountryEntity, CountryReq, CountryRes> {
    @Override
    public CountryEntity reqToEntity(CountryReq countryReq) {
        return new CountryEntity()
                .setName(countryReq.getName());
    }

    @Override
    public CountryRes entityToRes(CountryEntity countryEntity) {
        return new CountryRes()
                .setId(countryEntity.getId())
                .setName(countryEntity.getName());
    }

    @Override
    public CountryRes prepRes(CountryEntity countryEntity) {
        return entityToRes(countryEntity);
    }

    @Override
    public void setUpdatedValue(CountryReq countryReq, CountryEntity countryEntity) {
        if (countryReq != null && countryEntity != null) {
            if (countryReq.getName() != null && !countryReq.getName().equals(countryEntity.getName())) {
                countryEntity.setName(countryReq.getName());
            }
        }
    }
}
