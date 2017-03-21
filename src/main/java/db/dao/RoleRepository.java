package db.dao;

import db.entity.Role;
import manager.RoleCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Eric on 3/19/2017.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    public Role findByRoleCode(RoleCode roleCode);
}
