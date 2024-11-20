package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tbl_state")
public class StateEntity extends CommonEntity {

    @Column(name = "name", length = 1000, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;

    @OneToMany(mappedBy = "state", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<CityEntity> cities;

    public StateEntity setName(String name) {
        this.name = name;
        return this;
    }

    public StateEntity setCountry(CountryEntity country) {
        this.country = country;
        return this;
    }

    public StateEntity setCities(List<CityEntity> cities) {
        this.cities = cities;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StateEntity that = (StateEntity) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
