package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.RoleReq;
import se.ki.education.nkcx.dto.response.AuthorityRes;
import se.ki.education.nkcx.dto.response.RoleResDto;
import se.ki.education.nkcx.entity.AuthorityEntity;
import se.ki.education.nkcx.entity.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleDtoUtil implements DtoUtil<RoleEntity, RoleReq, RoleResDto> {

    private final AuthorityDtoUtil authorityDtoUtil;

    @Autowired
    public RoleDtoUtil(AuthorityDtoUtil authorityDtoUtil) {
        this.authorityDtoUtil = authorityDtoUtil;
    }

    @Override
    public RoleEntity reqToEntity(RoleReq roleReq) {
        return new RoleEntity()
                .setTitle(roleReq.getTitle());
    }

    @Override
    public RoleResDto entityToRes(RoleEntity roleEntity) {
        return new RoleResDto()
                .setId(roleEntity.getId())
                .setTitle(roleEntity.getTitle());
    }

    @Override
    public RoleResDto prepRes(RoleEntity roleEntity) {
        RoleResDto roleResDto = entityToRes(roleEntity);
        if (roleEntity.getAuthorities() != null && roleEntity.getAuthorities().size() > 0) {
            List<AuthorityRes> authorityRes = new ArrayList<>();
            for (AuthorityEntity authorityEntity : roleEntity.getAuthorities()) {
                authorityRes.add(authorityDtoUtil.entityToRes(authorityEntity));
            }
            roleResDto.setAuthorities(authorityRes);
        }
        return roleResDto;
    }

    @Override
    public void setUpdatedValue(RoleReq roleReq, RoleEntity roleEntity) {
        if (roleReq != null && roleEntity != null) {
            if (roleReq.getTitle() != null && !roleReq.getTitle().equals(roleEntity.getTitle())) {
                roleEntity.setTitle(roleReq.getTitle());
            }
        }
    }
}
