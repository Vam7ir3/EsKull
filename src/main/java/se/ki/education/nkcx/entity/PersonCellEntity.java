package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_person_cell")

public class PersonCellEntity extends CommonEntity{

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private PersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "cell_id", nullable = false)
    private CellEntity cellEntity;

    public PersonCellEntity setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
        return this;
    }

    public PersonCellEntity setCellEntity(CellEntity cellEntity) {
        this.cellEntity = cellEntity;
        return this;
    }
}
