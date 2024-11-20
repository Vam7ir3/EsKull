package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.AuthorityReq;
import se.ki.education.nkcx.dto.response.AuthorityRes;
import se.ki.education.nkcx.entity.AuthorityEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthorityDtoUtil implements DtoUtil<AuthorityEntity, AuthorityReq, AuthorityRes> {
    @Override
    public AuthorityEntity reqToEntity(AuthorityReq authorityReq) {
        return new AuthorityEntity()
                .setTitle(authorityReq.getTitle());
    }

    @Override
    public AuthorityRes entityToRes(AuthorityEntity authorityEntity) {
        return new AuthorityRes()
                .setId(authorityEntity.getId())
                .setTitle(authorityEntity.getTitle())
                .setDescription(authorityEntity.getDescription());
//                .setTitle(Authority.valueOf(authorityEntity.getTitle()).getDisplayTitle());
    }

    @Override
    public AuthorityRes prepRes(AuthorityEntity authorityEntity) {
        return entityToRes(authorityEntity);
    }

    @Override
    public void setUpdatedValue(AuthorityReq authorityReq, AuthorityEntity authorityEntity) {
        if (authorityReq != null && authorityEntity != null) {
            if (authorityReq.getTitle() != null && !authorityReq.getTitle().equals(authorityEntity.getTitle())) {
                authorityEntity.setTitle(authorityReq.getTitle());
                authorityEntity.setDescription(authorityReq.getDescription());
            }
        }
    }
}
