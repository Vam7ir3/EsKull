package se.ki.education.nkcx.dto.util.student;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.student.StudentReq;
import se.ki.education.nkcx.dto.response.student.StudentRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.student.StudentEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class StudentDtoUtil implements DtoUtil<StudentEntity, StudentReq, StudentRes> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public StudentEntity reqToEntity(StudentReq studentReq) {
        StudentEntity studentEntity = new StudentEntity()
                .setFirstName(studentReq.getFirstName())
                .setMiddleName(studentReq.getMiddleName())
                .setLastName(studentReq.getLastName())
                .setBatchNumber(studentReq.getBatchNumber())
                .setGrade(studentReq.getGrade())
                .setYear(studentReq.getYear());

        if(studentReq.getDateOfBirth() != null){
            try {
                LocalDate date = LocalDate.parse(studentReq.getDateOfBirth(), DATE_FORMATTER);
                long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                studentEntity.setDateOfBirth(epochDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
        return studentEntity;
    }

    @Override
    public StudentRes entityToRes(StudentEntity studentEntity) {
        StudentRes studentRes = new StudentRes()
                .setId(studentEntity.getId())
                .setFirstName(studentEntity.getFirstName())
                .setMiddleName(studentEntity.getMiddleName())
                .setLastName(studentEntity.getLastName())
                .setBatchNumber(studentEntity.getBatchNumber())
                .setGrade(studentEntity.getGrade())
                .setYear(studentEntity.getYear());

        if(studentEntity.getDateOfBirth() != null){
            LocalDate date = Instant.ofEpochMilli(studentEntity.getDateOfBirth()).atZone(ZoneId.systemDefault()).toLocalDate();
            studentRes.setDateOfBirth(date.format(DATE_FORMATTER));
        }
        return studentRes;
    }

    @Override
    public StudentRes prepRes(StudentEntity studentEntity) {
        return entityToRes(studentEntity);
    }

    @Override
    public void setUpdatedValue(StudentReq studentReq, StudentEntity studentEntity) {
        if(studentReq != null && studentEntity != null){
            if(studentReq.getFirstName() != null && !studentReq.getFirstName().equals(studentEntity.getFirstName())){
                studentEntity.setFirstName(studentReq.getFirstName());
            }

            if(studentReq.getMiddleName() != null && !studentReq.getMiddleName().equals(studentEntity.getMiddleName())){
                studentEntity.setMiddleName(studentReq.getMiddleName());
            }

            if(studentReq.getLastName() != null && !studentReq.getLastName().equals(studentEntity.getLastName())){
                studentEntity.setLastName(studentReq.getLastName());
            }

            if(studentReq.getBatchNumber() != null && !studentReq.getBatchNumber().equals(studentEntity.getBatchNumber())){
                studentEntity.setBatchNumber(studentReq.getBatchNumber());
            }

            if(studentReq.getGrade() != null && !studentReq.getGrade().equals(studentEntity.getGrade())){
                studentEntity.setGrade(studentReq.getGrade());
            }

            if (studentReq.getDateOfBirth() != null){
                LocalDate date  = LocalDate.parse(studentReq.getDateOfBirth(), DATE_FORMATTER);
                long epochDate = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
                studentEntity.setDateOfBirth(epochDate);
            }

            if(studentReq.getYear() != null && !studentReq.getYear().equals(studentEntity.getYear())){
                studentEntity.setYear(studentReq.getYear());
            }
        }
    }
}
