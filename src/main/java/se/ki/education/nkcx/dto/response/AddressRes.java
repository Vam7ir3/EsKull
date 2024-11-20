package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressRes {
    private String street;
    private CityRes city;

    public AddressRes setStreet(String street) {
        this.street = street;
        return this;
    }

    public AddressRes setCity(CityRes city) {
        this.city = city;
        return this;
    }
}
