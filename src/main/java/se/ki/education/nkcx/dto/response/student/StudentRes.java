package se.ki.education.nkcx.dto.response.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentRes {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String batchNumber;
    private String grade;
    private String dateOfBirth;
    private String year;

    public StudentRes setId(Long id) {
        this.id = id;
        return this;
    }

    public StudentRes setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentRes setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public StudentRes setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentRes setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
        return this;
    }

    public StudentRes setGrade(String grade) {
        this.grade = grade;
        return this;
    }

    public StudentRes setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public StudentRes setYear(String year) {
        this.year = year;
        return this;
    }
}
