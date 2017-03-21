package db.entity;

/**
 * Created by Eric on 3/19/2017.
 */
import manager.RoleCode;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_code")
    @Enumerated(EnumType.STRING)
    private RoleCode roleCode;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleCode getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(RoleCode roleCode) {
        this.roleCode = roleCode;
    }

    @Override
    public String getAuthority() {
        return roleCode.toString();
    }
}

