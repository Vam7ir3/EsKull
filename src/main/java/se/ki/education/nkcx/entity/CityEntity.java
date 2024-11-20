package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Data
@Entity
@Table(name = "tbl_city")
public class CityEntity extends CommonEntity {

    @Column(name = "name", length = 1000, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private StateEntity state;

    @OneToMany(mappedBy = "city", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<AddressEntity> addresses;

    public CityEntity setName(String name) {
        this.name = name;
        return this;
    }

    public CityEntity setState(StateEntity state) {
        this.state = state;
        return this;
    }

    public CityEntity setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
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
        CityEntity that = (CityEntity) o;
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
