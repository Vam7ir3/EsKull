package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.InvitationTypeReq;
import se.ki.education.nkcx.dto.response.InvitationTypeRes;
import se.ki.education.nkcx.entity.InvitationTypeEntity;

@Component
public class InvitationTypeDtoUtil implements DtoUtil<InvitationTypeEntity, InvitationTypeReq, InvitationTypeRes> {
    @Override
    public InvitationTypeEntity reqToEntity(InvitationTypeReq invitationTypeReq) {
        return new InvitationTypeEntity()
                .setType(invitationTypeReq.getType())
                .setXtype(invitationTypeReq.getXtype())
                .setDescription(invitationTypeReq.getDescription());
    }

    @Override
    public InvitationTypeRes entityToRes(InvitationTypeEntity invitationTypeEntity) {
        return new InvitationTypeRes()
                .setId(invitationTypeEntity.getId())
                .setType(invitationTypeEntity.getType())
                .setXtype(invitationTypeEntity.getXtype())
                .setDescription(invitationTypeEntity.getDescription());
    }

    @Override
    public InvitationTypeRes prepRes(InvitationTypeEntity invitationTypeEntity) {
        return entityToRes(invitationTypeEntity);
    }

    @Override
    public void setUpdatedValue(InvitationTypeReq invitationTypeReq, InvitationTypeEntity invitationTypeEntity) {
        if(invitationTypeReq != null && invitationTypeEntity != null) {
            if(invitationTypeReq.getType() != null && !invitationTypeReq.getType().equals(invitationTypeEntity.getType())) {
                invitationTypeEntity.setType(invitationTypeReq.getType());
            }
            if(invitationTypeReq.getXtype() != null && !invitationTypeReq.getXtype().equals(invitationTypeEntity.getXtype())) {
                invitationTypeEntity.setXtype(invitationTypeReq.getXtype());
            }
            if(invitationTypeReq.getDescription() != null && !invitationTypeReq.getDescription().equals(invitationTypeEntity.getDescription())) {
                invitationTypeEntity.setDescription(invitationTypeReq.getDescription());
            }
        }
    }
}
