package se.ki.education.nkcx.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageRes {
    private Long id;
    private String imageUrl;
    private boolean defaultImage;

    public ImageRes setId(Long id) {
        this.id = id;
        return this;
    }

    public ImageRes setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ImageRes setDefaultImage(boolean defaultImage) {
        this.defaultImage = defaultImage;
        return this;
    }
}
