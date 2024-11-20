package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KlartextRes {

    private Long id;
    private String snomedText;

    public KlartextRes setId(Long id) {
        this.id = id;
        return this;
    }

    public KlartextRes setSnomedText(String snomedText) {
        this.snomedText = snomedText;
        return this;
    }
}
