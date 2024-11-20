package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.AllowedRegistrationReq;
import se.ki.education.nkcx.dto.response.AllowedRegistrationRes;
import se.ki.education.nkcx.entity.AllowedRegistrationEntity;
import org.springframework.stereotype.Component;

@Component
public class AllowedRegistrationDtoUtil implements DtoUtil<AllowedRegistrationEntity, AllowedRegistrationReq, AllowedRegistrationRes> {

    @Override
    public AllowedRegistrationEntity reqToEntity(AllowedRegistrationReq allowedRegistrationReq) {
        return new AllowedRegistrationEntity()
                .setEmail(allowedRegistrationReq.getEmail());
    }

    @Override
    public AllowedRegistrationRes entityToRes(AllowedRegistrationEntity allowedRegistrationEntity) {
        return new AllowedRegistrationRes()
                .setId(allowedRegistrationEntity.getId())
                .setEmail(allowedRegistrationEntity.getEmail())
                .setCreatedDateTime(allowedRegistrationEntity.getCreatedDate())
                .setRegisteredDateTime(allowedRegistrationEntity.getRegisteredDateTime());
    }

    @Override
    public AllowedRegistrationRes prepRes(AllowedRegistrationEntity allowedRegistrationEntity) {
        return entityToRes(allowedRegistrationEntity);
    }

    @Override
    public void setUpdatedValue(AllowedRegistrationReq allowedRegistrationReq, AllowedRegistrationEntity allowedRegistrationEntity) {
        if (allowedRegistrationReq != null && allowedRegistrationEntity != null) {
            if (allowedRegistrationReq.getEmail() != null && !allowedRegistrationReq.getEmail().equals(allowedRegistrationEntity.getEmail())) {
                allowedRegistrationEntity.setEmail(allowedRegistrationReq.getEmail());
            }
        }
    }
}
