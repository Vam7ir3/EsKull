package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_klartext")
@DynamicInsert
@DynamicUpdate
public class KlartextEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "snomed_text", nullable = false)
    private String snomedText;

    public KlartextEntity setSnomedText(String snomedText) {
        this.snomedText = snomedText;
        return this;
    }
}
