package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "tbl_address")
public class AddressEntity extends CommonEntity {

    @Column(name = "street", nullable = false, length = 1000)
    private String street;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;

    public AddressEntity setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressEntity setCity(CityEntity city) {
        this.city = city;
        return this;
    }
}
