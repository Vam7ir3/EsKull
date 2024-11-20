package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tbl_country")
public class CountryEntity extends CommonEntity {

    @Column(name = "name", length = 2000, unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "country", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<StateEntity> states;

    public CountryEntity setName(String name) {
        this.name = name;
        return this;
    }

    public CountryEntity setStates(List<StateEntity> states) {
        this.states = states;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CountryEntity)) {
            return false;
        }
        CountryEntity that = (CountryEntity) o;
        return this == that || Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
