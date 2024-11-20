package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;


@Data
@Entity
@Table(name = "tbl_person")
@DynamicInsert
@DynamicUpdate

public class PersonEntity extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth", nullable = false)
    private Long dateOfBirth;

    @Column(name = "pnr", nullable = false)
    private Integer pnr;

    @Column(name = "is_validpnr", nullable = false )
    private Boolean isValidPNR;

    @Column(name = "is_by_year")
    private Boolean isByYear;

    public PersonEntity setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public PersonEntity setPnr(Integer pnr) {
        this.pnr = pnr;
        return this;
    }

    public PersonEntity setIsValidPNR(Boolean isValidPNR) {
        this.isValidPNR = isValidPNR;
        return this;
    }

    public PersonEntity setIsByYear(Boolean isByYear) {
        this.isByYear = isByYear;
        return this;
    }

}
