package se.ki.education.nkcx.entity.student;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import se.ki.education.nkcx.entity.CommonEntity;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_student")
@DynamicInsert
@DynamicUpdate
public class StudentEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "batch_number", nullable = false)
    private String batchNumber;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "date_of_birth", nullable = false)
    private Long dateOfBirth;

    @Column(name = "year", nullable = false)
    private String year;

    public StudentEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentEntity setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public StudentEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentEntity setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
        return this;
    }

    public StudentEntity setGrade(String grade) {
        this.grade = grade;
        return this;
    }

    public StudentEntity setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public StudentEntity setYear(String year) {
        this.year = year;
        return this;
    }
}
