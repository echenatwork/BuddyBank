package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import web.auth.BuddyBankAuthenticationProvider;
import web.auth.PasswordTool;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BuddyBankAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    // Password hashing example https://www.mkyong.com/spring-security/spring-security-password-hashing-example/
    // Custom authentication manager example http://www.baeldung.com/spring-security-authentication-provider
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public BuddyBankAuthenticationProvider buddyBankAuthenticationProvider() {
        return new BuddyBankAuthenticationProvider();
    }

    @Bean
    public PasswordTool passwordTool() {
        return new PasswordTool();
    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
//        auth.inMemoryAuthentication().withUser("user2").password("password2").roles("USER");
//    }

    // TODO db auth
    // http://www.mkyong.com/spring-security/spring-security-form-login-using-database/
//    @Autowired
//    DataSource dataSource;
//
//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth.jdbcAuthentication().dataSource(dataSource)
//                .usersByUsernameQuery(
//                        "select username,password, enabled from users where username=?")
//                .authoritiesByUsernameQuery(
//                        "select username, role from user_roles where username=?");
//    }
}
