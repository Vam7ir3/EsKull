package se.ki.education.nkcx.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleReq {
    private Long id;
    private String title;
    private List<Long> authorityIds;
}
