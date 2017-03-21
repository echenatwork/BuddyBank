package config;

/**
 * Created by Eric on 3/12/2017.
 */

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories("db.dao")
@EntityScan(basePackages = {"db.entity"})
public class DatabaseConfig {

    // TODO the mysql-connector-java in the pom should only be runtime
    // TODO Move the db connection information out to config file
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/buddybank");
        dataSource.setUser("buddybank_app_user");
        dataSource.setPassword("temp_password");
        return dataSource;
    }

}
