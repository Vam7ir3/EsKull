package se.ki.education.nkcx.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_invitation_type")
@DynamicInsert
@DynamicUpdate
public class InvitationTypeEntity extends CommonEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "x_type", nullable = false)
    private String xtype;

    @Column(name = "description", nullable = false)
    private String description;

    public InvitationTypeEntity setType(String type) {
        this.type = type;
        return this;
    }

    public InvitationTypeEntity setXtype(String xtype) {
        this.xtype = xtype;
        return this;
    }

    public InvitationTypeEntity setDescription(String description) {
        this.description = description;
        return this;
    }
}
