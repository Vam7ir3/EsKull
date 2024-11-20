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
public class Cell6923DtoUtil implements DtoUtil<Cell6923Entity, Cell6923Req, Cell6923Res> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil;
    private final DtoUtil<LaboratoryEntity, LaboratoryReq, LaboratoryRes> laboratryDtoUtil;
    private final DtoUtil<CountyEntity, CountyReq, CountyRes> countyDtoUtil;
    private final DtoUtil<ReferenceTypeEntity, ReferenceTypeReq, ReferenceTypeRes> referenceTypeDtoUtil;

    public Cell6923DtoUtil(DtoUtil<PersonEntity, PersonReq, PersonRes> personDtoUtil, DtoUtil<LaboratoryEntity, LaboratoryReq, LaboratoryRes> laboratryDtoUtil, DtoUtil<CountyEntity, CountyReq, CountyRes> countyDtoUtil, DtoUtil<ReferenceTypeEntity, ReferenceTypeReq, ReferenceTypeRes> referenceTypeDtoUtil) {
        this.personDtoUtil = personDtoUtil;
        this.laboratryDtoUtil = laboratryDtoUtil;
        this.countyDtoUtil = countyDtoUtil;
        this.referenceTypeDtoUtil = referenceTypeDtoUtil;
    }


    @Override
    public Cell6923Entity reqToEntity(Cell6923Req cell6923Req) {
        Cell6923Entity cell6923Entity = new Cell6923Entity()
                .setSampleType(cell6923Req.getSampleType())
                .setReferralNumber(cell6923Req.getReferralNumber())
                .setReferenceSite(cell6923Req.getReferenceSite())
                .setResidc(cell6923Req.getResidc())
                .setResidk(cell6923Req.getResidk())
                .setXSnomed(cell6923Req.getXSnomed())
                .setDiagId(cell6923Req.getDiagId())
                .setAnsClinic(cell6923Req.getAnsClinic())
                .setDebClinic(cell6923Req.getDebClinic())
                .setRemClinic(cell6923Req.getRemClinic())
                .setScrType(cell6923Req.getScrType())
                .setSnomed(cell6923Req.getSnomed())
                .setDiffDays(cell6923Req.getDiffDays());


        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(cell6923Req.getPersonId());
        cell6923Entity.setPersonEntity(personEntity);

        LaboratoryEntity laboratoryEntity = new LaboratoryEntity();
        laboratoryEntity.setId(cell6923Req.getLaboratoryId());
        cell6923Entity.setLaboratoryEntity(laboratoryEntity);

        CountyEntity countyEntity = new CountyEntity();
        countyEntity.setId(cell6923Req.getCountyId());
        cell6923Entity.setCountyEntity(countyEntity);

        ReferenceTypeEntity referenceTypeEntity = new ReferenceTypeEntity();
        referenceTypeEntity.setId(cell6923Req.getReferralTypeId());
        cell6923Entity.setReferenceTypeEntity(referenceTypeEntity);

        if(cell6923Req.getSampleDate() != null){
            cell6923Entity.setSampleDate(parseDateToEpochMillis(cell6923Req.getSampleDate()));
        }

        if(cell6923Req.getXSampleDate() != null){
            cell6923Entity.setXSampleDate(parseDateToEpochMillis(cell6923Req.getXSampleDate()));
        }

        if(cell6923Req.getXRegistrationDate() != null){
            cell6923Entity.setXRegistrationDate(parseDateToEpochMillis(cell6923Req.getXRegistrationDate()));
        }

        if(cell6923Req.getXResponseDate() != null){
            cell6923Entity.setXResponseDate(parseDateToEpochMillis(cell6923Req.getXResponseDate()));
        }

        if (cell6923Req.getSampleType() != null && !cell6923Req.getSampleType().equals(cell6923Entity.getSampleType())) {
            cell6923Entity.setSampleType(cell6923Req.getSampleType());
        }



        if(cell6923Req.getRegistrationDate() != null){
            try{
                LocalDate date = LocalDate.parse(cell6923Req.getRegistrationDate(), DATE_FORMATTER);
                long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                cell6923Entity.setRegistrationDate(epochDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        if(cell6923Req.getResponseDate() != null){
            try{
                LocalDate date = LocalDate.parse(cell6923Req.getResponseDate(), DATE_FORMATTER);
                long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                cell6923Entity.setResponseDate(epochDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
            }
        }

        return cell6923Entity;
    }

    @Override
    public Cell6923Res entityToRes(Cell6923Entity cell6923Entity) {
        PersonRes personRes = personDtoUtil.entityToRes(cell6923Entity.getPersonEntity());
        LaboratoryRes laboratoryRes = laboratryDtoUtil.entityToRes(cell6923Entity.getLaboratoryEntity());
        CountyRes countyRes = countyDtoUtil.entityToRes(cell6923Entity.getCountyEntity());
        ReferenceTypeRes referenceTypeRes = referenceTypeDtoUtil.entityToRes(cell6923Entity.getReferenceTypeEntity());
        Cell6923Res cell6923Res = new Cell6923Res()
                .setId(cell6923Entity.getId())
                .setSampleType(cell6923Entity.getSampleType())
                .setReferralNumber(cell6923Entity.getReferralNumber())
                .setReferenceSite(cell6923Entity.getReferenceSite())
                .setResidc(cell6923Entity.getResidc())
                .setResidk(cell6923Entity.getResidk())
                .setXSnomed(cell6923Entity.getXSnomed())
                .setDiagId(cell6923Entity.getDiagId())
                .setAnsClinic(cell6923Entity.getAnsClinic())
                .setDebClinic(cell6923Entity.getDebClinic())
                .setRemClinic(cell6923Entity.getRemClinic())
                .setScrType(cell6923Entity.getScrType())
                .setSnomed(cell6923Entity.getSnomed())
                .setDiffDays(cell6923Entity.getDiffDays())
                .setPersonRes(personRes)
                .setLaboratoryRes(laboratoryRes)
                .setCountyRes(countyRes)
                .setReferralTypeId(referenceTypeRes);
        if (cell6923Entity.getSampleDate() != 0) {
            LocalDate date = Instant.ofEpochMilli(cell6923Entity.getSampleDate()).atZone(ZoneId.systemDefault()).toLocalDate();
            cell6923Res.setSampleDate(date.format(DATE_FORMATTER));
        }
        if (cell6923Entity.getXSampleDate() != 0) {
            LocalDate date = Instant.ofEpochMilli(cell6923Entity.getXSampleDate()).atZone(ZoneId.systemDefault()).toLocalDate();
            cell6923Res.setXSampleDate(date.format(DATE_FORMATTER));
        }
        if (cell6923Entity.getRegistrationDate() != 0) {
            LocalDate date = Instant.ofEpochMilli(cell6923Entity.getRegistrationDate()).atZone(ZoneId.systemDefault()).toLocalDate();
            cell6923Res.setRegistrationDate(date.format(DATE_FORMATTER));
        }
        if (cell6923Entity.getXRegistrationDate() != 0) {
            LocalDate date = Instant.ofEpochMilli(cell6923Entity.getXRegistrationDate()).atZone(ZoneId.systemDefault()).toLocalDate();
            cell6923Res.setXRegistrationDate(date.format(DATE_FORMATTER));
        }

        if (cell6923Entity.getResponseDate() != 0) {
            LocalDate date = Instant.ofEpochMilli(cell6923Entity.getResponseDate()).atZone(ZoneId.systemDefault()).toLocalDate();
            cell6923Res.setResponseDate(date.format(DATE_FORMATTER));
        }
        if (cell6923Entity.getXResponseDate() != 0) {
            LocalDate date = Instant.ofEpochMilli(cell6923Entity.getXResponseDate()).atZone(ZoneId.systemDefault()).toLocalDate();
            cell6923Res.setXResponseDate(date.format(DATE_FORMATTER));
        }

        return cell6923Res;

    }

    @Override
    public Cell6923Res prepRes(Cell6923Entity cell6923Entity) {
        return entityToRes(cell6923Entity);
    }

    @Override
    public void setUpdatedValue(Cell6923Req cell6923Req, Cell6923Entity cell6923Entity) {
        if(cell6923Req.getPersonId() != null){
            PersonEntity personEntity = new PersonEntity();
            personEntity.setId(cell6923Req.getPersonId());
            cell6923Entity.setPersonEntity(personEntity);
        }

        if(cell6923Req.getLaboratoryId() != null){
            LaboratoryEntity laboratoryEntity = new LaboratoryEntity();
            laboratoryEntity.setId(cell6923Req.getLaboratoryId());
            cell6923Entity.setLaboratoryEntity(laboratoryEntity);
        }

        if(cell6923Req.getCountyId() != null){
            CountyEntity countyEntity = new CountyEntity();
            countyEntity.setId(cell6923Req.getCountyId());
            cell6923Entity.setCountyEntity(countyEntity);
        }

        if(cell6923Req.getReferralTypeId() != null){
            ReferenceTypeEntity referenceTypeEntity = new ReferenceTypeEntity();
            referenceTypeEntity.setId(cell6923Req.getReferralTypeId());
            cell6923Entity.setReferenceTypeEntity(referenceTypeEntity);
        }

        if(cell6923Req.getSampleDate() != null){
            cell6923Entity.setSampleDate(parseDateToEpochMillis(cell6923Req.getSampleDate()));
        }

        if(cell6923Req.getXSampleDate() != null){
            cell6923Entity.setXSampleDate(parseDateToEpochMillis(cell6923Req.getXSampleDate()));
        }


        if(cell6923Req.getRegistrationDate() != null){
            LocalDate date = LocalDate.parse(cell6923Req.getRegistrationDate(), DATE_FORMATTER);
            long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
            cell6923Entity.setRegistrationDate(epochDate);
        }

        if(cell6923Req.getXRegistrationDate() != null){
            LocalDate date = LocalDate.parse(cell6923Req.getXRegistrationDate(), DATE_FORMATTER);
            long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
            cell6923Entity.setXRegistrationDate(epochDate);
        }

        if(cell6923Req.getResponseDate() != null){
            LocalDate date = LocalDate.parse(cell6923Req.getResponseDate(), DATE_FORMATTER);
            long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
            cell6923Entity.setResponseDate(epochDate);
        }

        if(cell6923Req.getXResponseDate() != null){
            LocalDate date = LocalDate.parse(cell6923Req.getXResponseDate(), DATE_FORMATTER);
            long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
            cell6923Entity.setXResponseDate(epochDate);
        }

        if (cell6923Req.getSampleType() != null && !cell6923Req.getSampleType().equals(cell6923Entity.getSampleType())) {
            cell6923Entity.setSampleType(cell6923Req.getSampleType());
        }

        if (cell6923Req.getReferralNumber() != null && !cell6923Req.getReferralNumber().equals(cell6923Entity.getReferralNumber())) {
            cell6923Entity.setReferralNumber(cell6923Req.getReferralNumber());
        }

        if (cell6923Req.getReferenceSite() != null && !cell6923Req.getReferenceSite().equals(cell6923Entity.getReferenceSite())) {
            cell6923Entity.setReferenceSite(cell6923Req.getReferenceSite());
        }

        if (cell6923Req.getResidc() != null && !cell6923Req.getResidc().equals(cell6923Entity.getResidc())) {
            cell6923Entity.setResidc(cell6923Req.getResidc());
        }

        if (cell6923Req.getResidk() != null && !cell6923Req.getResidk().equals(cell6923Entity.getResidk())) {
            cell6923Entity.setResidk(cell6923Req.getResidk());
        }

        if (cell6923Req.getXSnomed() != null && !cell6923Req.getXSnomed().equals(cell6923Entity.getXSnomed())) {
            cell6923Entity.setXSnomed(cell6923Req.getXSnomed());
        }

        if (cell6923Req.getDiagId() != null && !cell6923Req.getDiagId().equals(cell6923Entity.getDiagId())) {
            cell6923Entity.setDiagId(cell6923Req.getDiagId());
        }

        if (cell6923Req.getAnsClinic() != null && !cell6923Req.getAnsClinic().equals(cell6923Entity.getAnsClinic())) {
            cell6923Entity.setAnsClinic(cell6923Req.getAnsClinic());
        }

        if (cell6923Req.getDebClinic() != null && !cell6923Req.getDebClinic().equals(cell6923Entity.getDebClinic())) {
            cell6923Entity.setDebClinic(cell6923Req.getDebClinic());
        }

        if (cell6923Req.getRemClinic() != null && !cell6923Req.getRemClinic().equals(cell6923Entity.getRemClinic())) {
            cell6923Entity.setRemClinic(cell6923Req.getRemClinic());
        }

        if (cell6923Req.getScrType() != null && !cell6923Req.getScrType().equals(cell6923Entity.getScrType())) {
            cell6923Entity.setScrType(cell6923Req.getScrType());
        }

        if (cell6923Req.getSnomed() != null && !cell6923Req.getSnomed().equals(cell6923Entity.getSnomed())) {
            cell6923Entity.setSnomed(cell6923Req.getSnomed());
        }

        if (cell6923Req.getDiffDays() != null && !cell6923Req.getDiffDays().equals(cell6923Entity.getDiffDays())) {
            cell6923Entity.setDiffDays(cell6923Req.getDiffDays());
        }
    }

    private long parseDateToEpochMillis(String dateStr){
        if (dateStr != null){
            try{
                LocalDate localDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                return localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return 0;
    }
}
