package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.StateReq;
import se.ki.education.nkcx.dto.response.StateResDto;
import se.ki.education.nkcx.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateDtoUtil implements DtoUtil<StateEntity, StateReq, StateResDto> {

    private final CountryDtoUtil countryDtoUtil;

    @Autowired
    public StateDtoUtil(CountryDtoUtil countryDtoUtil) {
        this.countryDtoUtil = countryDtoUtil;
    }

    @Override
    public StateEntity reqToEntity(StateReq stateReq) {
        return new StateEntity()
                .setName(stateReq.getName());
    }

    @Override
    public StateResDto entityToRes(StateEntity stateEntity) {
        return new StateResDto()
                .setId(stateEntity.getId())
                .setName(stateEntity.getName());
    }

    @Override
    public StateResDto prepRes(StateEntity stateEntity) {
        return entityToRes(stateEntity)
                .setCountry(countryDtoUtil.entityToRes(stateEntity.getCountry()));
    }

    @Override
    public void setUpdatedValue(StateReq stateReq, StateEntity stateEntity) {
        if (stateReq != null && stateEntity != null) {
            if (stateReq.getName() != null && !stateReq.getName().equals(stateEntity.getName())) {
                stateEntity.setName(stateReq.getName());
            }
        }
    }
}
