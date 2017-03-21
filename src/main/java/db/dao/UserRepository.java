package db.dao;

import db.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Eric on 3/12/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    public User findByUserName(String userName);

}
