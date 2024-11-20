package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tbl_multi_lang_message")
@DynamicInsert
@DynamicUpdate
public class MultiLangMessageEntity extends CommonEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "english", nullable = false)
    private String english;

    @Column(name = "swedish")
    private String swedish;

    @Column(name = "spanish")
    private String spanish;

    public MultiLangMessageEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public MultiLangMessageEntity setEnglish(String english) {
        this.english = english;
        return this;
    }

    public MultiLangMessageEntity setSwedish(String swedish) {
        this.swedish = swedish;
        return this;
    }

    public MultiLangMessageEntity setSpanish(String spanish) {
        this.spanish = spanish;
        return this;
    }
}
