package se.ki.education.nkcx.dto.request;

import lombok.Data;

@Data
public class ImageReq {
    private Long id;
    private String image;//base64
    private Boolean defaultImage;
}
