package se.ki.education.nkcx.dto.request.student;

import lombok.Data;

@Data
public class StudentReq {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String batchNumber;
    private String grade;
    private String dateOfBirth;
    private String year;
}