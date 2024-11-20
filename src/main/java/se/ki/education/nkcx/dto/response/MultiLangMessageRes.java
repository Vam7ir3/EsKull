package se.ki.education.nkcx.dto.response;

import lombok.Data;

@Data
public class MultiLangMessageRes {
    private Long id;
    private String code;
    private String english;
    private String swedish;
    private String spanish;

    public MultiLangMessageRes setId(Long id) {
        this.id = id;
        return this;
    }

    public MultiLangMessageRes setCode(String code) {
        this.code = code;
        return this;
    }

    public MultiLangMessageRes setEnglish(String english) {
        this.english = english;
        return this;
    }

    public MultiLangMessageRes setSwedish(String swedish) {
        this.swedish = swedish;
        return this;
    }

    public MultiLangMessageRes setSpanish(String spanish) {
        this.spanish = spanish;
        return this;
    }
}
