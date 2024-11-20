package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.AuthorityReq;
import se.ki.education.nkcx.dto.request.RoleReq;
import se.ki.education.nkcx.dto.request.UserReq;
import se.ki.education.nkcx.dto.response.AuthorityRes;
import se.ki.education.nkcx.dto.response.RoleResDto;
import se.ki.education.nkcx.dto.response.UserResDto;
import se.ki.education.nkcx.entity.AuthorityEntity;
import se.ki.education.nkcx.entity.RoleEntity;
import se.ki.education.nkcx.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoUtil implements DtoUtil<UserEntity, UserReq, UserResDto> {

    private final DtoUtil<RoleEntity, RoleReq, RoleResDto> roleDtoUtil;
    private final DtoUtil<AuthorityEntity, AuthorityReq, AuthorityRes> authorityDtoUtil;

    @Autowired
    public UserDtoUtil(DtoUtil<RoleEntity, RoleReq, RoleResDto> roleDtoUtil, DtoUtil<AuthorityEntity, AuthorityReq, AuthorityRes> authorityDtoUtil) {
        this.roleDtoUtil = roleDtoUtil;
        this.authorityDtoUtil = authorityDtoUtil;
    }

    @Override
    public UserEntity reqToEntity(UserReq userReq) {
        return new UserEntity()
                .setFirstName(userReq.getFirstName())
                .setLastName(userReq.getLastName())
                .setGender(userReq.getGender())
                .setDateOfBirth(userReq.getDateOfBirth())
                .setMobileNumber(userReq.getMobileNumber())
                .setEmailAddress(userReq.getEmailAddress());
    }

    @Override
    public UserResDto entityToRes(UserEntity userEntity) {
        return new UserResDto()
                .setId(userEntity.getId())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName())
                .setGender(userEntity.getGender())
                .setDateOfBirth(userEntity.getDateOfBirth())
                .setMobileNumber(userEntity.getMobileNumber())
                .setEmailAddress(userEntity.getEmailAddress())
                .setImageUrl(userEntity.getImageUrl())
                .setCreatedDateTime(userEntity.getCreatedDate());
    }

    @Override
    public UserResDto prepRes(UserEntity userEntity) {
        UserResDto userResDto = entityToRes(userEntity);

        //Role and Authority
        RoleResDto roleResDto = roleDtoUtil.entityToRes(userEntity.getRole());
        List<AuthorityRes> authorityRes = new ArrayList<>();
        userEntity.getRole().getAuthorities().forEach(authorityEntity -> authorityRes.add(authorityDtoUtil.entityToRes(authorityEntity)));
        roleResDto.setAuthorities(authorityRes);

        userResDto.setRole(roleResDto);


        return userResDto;
    }

    @Override
    public void setUpdatedValue(UserReq userReq, UserEntity userEntity) {
        if (userReq.getFirstName() != null && !userReq.getFirstName().equals(userEntity.getFirstName())) {
            userEntity.setFirstName(userReq.getFirstName());
        }

        if (userReq.getLastName() != null && !userReq.getLastName().equals(userEntity.getLastName())) {
            userEntity.setLastName(userReq.getLastName());
        }

        if (userReq.getGender() != null &&
                (userEntity.getGender() == null || !userReq.getGender().equals(userEntity.getGender()))) {
            userEntity.setGender(userReq.getGender());
        }

        if (userReq.getDateOfBirth() != null &&
                (userEntity.getDateOfBirth() == null || !userReq.getDateOfBirth().equals(userEntity.getDateOfBirth()))) {
            userEntity.setDateOfBirth(userReq.getDateOfBirth());
        }

        if (userReq.getMobileNumber() != null &&
                (userEntity.getMobileNumber() == null || !userReq.getMobileNumber().equals(userEntity.getMobileNumber()))) {
            userEntity.setMobileNumber(userReq.getMobileNumber());
        }
        if (userReq.getEmailAddress() != null && !userReq.getEmailAddress().equals(userEntity.getEmailAddress())) {
            userEntity.setEmailAddress(userReq.getEmailAddress());
        }
    }
}
