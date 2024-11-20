package se.ki.education.nkcx.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "tbl_role")
public class RoleEntity extends CommonEntity {

    @Column(name = "title", length = 500, nullable = false, unique = true)
    private String title;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_role_authority",
            joinColumns = {@JoinColumn(name = "role_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", nullable = false)}
    )
    private List<AuthorityEntity> authorities;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<UserEntity> users;

    public RoleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public RoleEntity setAuthorities(List<AuthorityEntity> authorities) {
        this.authorities = authorities;
        return this;
    }

    public RoleEntity setUsers(List<UserEntity> users) {
        this.users = users;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoleEntity)) {
            return false;
        }
        RoleEntity that = (RoleEntity) o;
        return this == that || Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
