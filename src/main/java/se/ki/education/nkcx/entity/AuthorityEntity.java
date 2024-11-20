package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tbl_authority")
public class AuthorityEntity extends CommonEntity {

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(mappedBy = "authorities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RoleEntity> roles;

//    public AuthorityEntity(String authority) {
//
//    }
//
//    public AuthorityEntity() {
//
//    }

    public AuthorityEntity setTitle(String title) {
        this.title = title;
        return this;
    }
    public AuthorityEntity setDescription(String description) {
        this.description = description;
        return this;
    }


    public AuthorityEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AuthorityEntity)) {
            return false;
        }
        AuthorityEntity that = (AuthorityEntity) o;
        return this == that || Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
