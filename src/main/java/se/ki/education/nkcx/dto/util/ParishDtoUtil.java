package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.*;
import se.ki.education.nkcx.dto.response.*;
import se.ki.education.nkcx.entity.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class ParishDtoUtil implements DtoUtil<ParishEntity, ParishReq, ParishRes> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DtoUtil<MunicipalityEntity, MunicipalityReq, MunicipalityRes> municipalityDtoUtil;
    private final DtoUtil<CountyEntity, CountyReq, CountyRes> countyDtoUtil;

    public ParishDtoUtil(DtoUtil<MunicipalityEntity, MunicipalityReq, MunicipalityRes> municipalityDtoUtil, DtoUtil<CountyEntity, CountyReq, CountyRes> countyDtoUtil) {
        this.municipalityDtoUtil = municipalityDtoUtil;
        this.countyDtoUtil = countyDtoUtil;
    }


    @Override
    public ParishEntity reqToEntity(ParishReq parishReq) {
        ParishEntity parishEntity = new ParishEntity()
                .setName(parishReq.getName())
                .setDividedOtherCounty(parishReq.getDividedOtherCounty());

        if (parishReq.getRegisterDate() != null) {
            try {
                LocalDate date = LocalDate.parse(parishReq.getRegisterDate(), DATE_FORMATTER);
                long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                parishEntity.setRegisterDate(epochDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        if (parishReq.getMunicipalityId() != null) {
            MunicipalityEntity municipalityEntity = new MunicipalityEntity();
            municipalityEntity.setId(parishReq.getMunicipalityId());
            parishEntity.setMunicipalityEntity(municipalityEntity);
        }

        if (parishReq.getCountyId() != null) {
            CountyEntity countyEntity = new CountyEntity();
            countyEntity.setId(parishReq.getCountyId());
            parishEntity.setCountyEntity(countyEntity);
        }
        return parishEntity;
    }

    @Override
    public ParishRes entityToRes(ParishEntity parishEntity) {
        MunicipalityRes municipalityRes = null;
        CountyRes countyRes = null;

        if (parishEntity.getMunicipalityEntity() != null) {
            municipalityRes = municipalityDtoUtil.entityToRes(parishEntity.getMunicipalityEntity());
        }
        if (parishEntity.getCountyEntity() != null) {
            countyRes = countyDtoUtil.entityToRes(parishEntity.getCountyEntity());
        }
        ParishRes parishRes = new ParishRes()
                .setId(parishEntity.getId())
                .setName(parishEntity.getName())
                .setDividedOtherCounty(parishEntity.getDividedOtherCounty())
                .setMunicipalityRes(municipalityRes)
                .setCountyRes(countyRes);

        if (parishEntity.getRegisterDate() != 0) {
            LocalDate date = Instant.ofEpochMilli(parishEntity.getRegisterDate()).atZone(ZoneId.systemDefault()).toLocalDate();
            parishRes.setRegisterDate(date.format(DATE_FORMATTER));
        }
        return parishRes;
    }

    @Override
    public ParishRes prepRes(ParishEntity parishEntity) {
        return entityToRes(parishEntity);
    }

    @Override
    public void setUpdatedValue(ParishReq parishReq, ParishEntity parishEntity) {
        if (parishReq != null && parishEntity != null) {

            if (parishReq.getName() != null && !parishReq.getName().equals(parishEntity.getName())) {
                parishEntity.setName(parishReq.getName());
            }
            if (parishReq.getRegisterDate() != null){
                LocalDate date  = LocalDate.parse(parishReq.getRegisterDate(), DATE_FORMATTER);
                long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                parishEntity.setRegisterDate(epochDate);
            }
            if (parishReq.getDividedOtherCounty() != null && !parishReq.getDividedOtherCounty().equals(parishEntity.getDividedOtherCounty())) {
                parishEntity.setDividedOtherCounty(parishReq.getDividedOtherCounty());
            }
            if (parishReq.getMunicipalityId() != null) {
                MunicipalityEntity municipalityEntity = new MunicipalityEntity();
                municipalityEntity.setId(parishReq.getMunicipalityId());
                parishEntity.setMunicipalityEntity(municipalityEntity);
            } else {
                parishEntity.setMunicipalityEntity(null);
            }
            if (parishReq.getCountyId() != null) {
                CountyEntity countyEntity = new CountyEntity();
                countyEntity.setId(parishReq.getCountyId());
                parishEntity.setCountyEntity(countyEntity);
            } else {
                parishEntity.setCountyEntity(null);
            }
        }
    }
}
