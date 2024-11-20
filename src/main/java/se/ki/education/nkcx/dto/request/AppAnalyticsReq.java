package se.ki.education.nkcx.dto.request;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class AppAnalyticsReq {
    private Long id;
    private String description;

    @NotNull
    private String value;

    private Long appAnalyticsKeyId;
}
