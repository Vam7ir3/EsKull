package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.entity.PersonEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class PersonDtoUtil implements DtoUtil<PersonEntity, PersonReq, PersonRes> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public PersonEntity reqToEntity(PersonReq personReq) {
        PersonEntity personEntity = new PersonEntity()
                .setPnr(personReq.getPnr())
                .setIsValidPNR(personReq.getIsValidPNR())
                .setIsByYear(personReq.getIsByYear());
        if (personReq.getDateOfBirth() != null) {
            try {
                LocalDate date = LocalDate.parse(personReq.getDateOfBirth(), DATE_FORMATTER);
                long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                personEntity.setDateOfBirth(epochDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return personEntity;
    }

    @Override
    public PersonRes entityToRes(PersonEntity personEntity) {
        PersonRes personRes = new PersonRes()
                .setId(personEntity.getId())
                .setPnr(personEntity.getPnr())
                .setValidPNR(personEntity.getIsValidPNR())
                .setIsByYear(personEntity.getIsByYear());
        if (personEntity.getDateOfBirth() != 0) {
            LocalDate date = Instant.ofEpochMilli(personEntity.getDateOfBirth()).atZone(ZoneId.systemDefault()).toLocalDate();
            personRes.setDateOfBirth(date.format(DATE_FORMATTER));
        }
        return personRes;
    }

    @Override
    public PersonRes prepRes(PersonEntity personEntity) {
        return entityToRes(personEntity);
    }

    @Override
    public void setUpdatedValue(PersonReq personReq, PersonEntity personEntity) {
        if (personReq != null && personEntity != null) {
            if (personReq.getPnr() != null && !personReq.getPnr().equals(personEntity.getPnr())) {
                personEntity.setPnr(personReq.getPnr());
            }
            if (personReq.getIsValidPNR() != null && !personReq.getIsValidPNR().equals(personEntity.getIsValidPNR())) {
                personEntity.setIsValidPNR(personReq.getIsValidPNR());
            }
            if (personReq.getDateOfBirth() != null){
                    LocalDate date  = LocalDate.parse(personReq.getDateOfBirth(), DATE_FORMATTER);
                    long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                    personEntity.setDateOfBirth(epochDate);
            }

            if (personReq.getIsByYear() != null && !personReq.getIsByYear().equals(personEntity.getIsByYear())) {
                personEntity.setIsByYear(personReq.getIsByYear());
            }
        }
    }
}
