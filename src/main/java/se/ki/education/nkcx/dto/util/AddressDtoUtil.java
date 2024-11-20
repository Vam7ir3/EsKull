package se.ki.education.nkcx.dto.util;


import se.ki.education.nkcx.dto.request.CityReq;
import se.ki.education.nkcx.dto.request.CountryReq;
import se.ki.education.nkcx.dto.request.StateReq;
import se.ki.education.nkcx.dto.response.AddressRes;
import se.ki.education.nkcx.dto.response.CityRes;
import se.ki.education.nkcx.dto.response.CountryRes;
import se.ki.education.nkcx.dto.response.StateResDto;
import se.ki.education.nkcx.entity.AddressEntity;
import se.ki.education.nkcx.entity.CityEntity;
import se.ki.education.nkcx.entity.CountryEntity;
import se.ki.education.nkcx.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoUtil implements DtoUtil<AddressEntity, Object, AddressRes> {

    private final DtoUtil<CountryEntity, CountryReq, CountryRes> countryDtoUtil;
    private final DtoUtil<StateEntity, StateReq, StateResDto> stateDtoUtil;
    private final DtoUtil<CityEntity, CityReq, CityRes> cityDtoUtil;

    @Autowired
    public AddressDtoUtil(DtoUtil<CountryEntity, CountryReq, CountryRes> countryDtoUtil, DtoUtil<StateEntity, StateReq, StateResDto> stateDtoUtil, DtoUtil<CityEntity, CityReq, CityRes> cityDtoUtil) {
        this.countryDtoUtil = countryDtoUtil;
        this.stateDtoUtil = stateDtoUtil;
        this.cityDtoUtil = cityDtoUtil;
    }

    @Override
    public AddressEntity reqToEntity(Object o) {
        return null;
    }

    @Override
    public AddressRes entityToRes(AddressEntity addressEntity) {
        return null;
    }

    @Override
    public AddressRes prepRes(AddressEntity addressEntity) {
        CountryRes countryRes = countryDtoUtil.entityToRes(addressEntity.getCity().getState().getCountry());

        StateResDto stateResDto = stateDtoUtil.entityToRes(addressEntity.getCity().getState());
        stateResDto.setCountry(countryRes);

        CityRes cityRes = cityDtoUtil.entityToRes(addressEntity.getCity());
        cityRes.setState(stateResDto);

        AddressRes addressRes = new AddressRes();
        addressRes.setStreet(addressEntity.getStreet());
        addressRes.setCity(cityRes);

        return addressRes;
    }

    @Override
    public void setUpdatedValue(Object o, AddressEntity addressEntity) {

    }
}
