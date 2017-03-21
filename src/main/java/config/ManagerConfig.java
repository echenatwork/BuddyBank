package config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Eric on 3/19/2017.
 */
@Configuration
@ComponentScan(basePackages = {"manager"})
public class ManagerConfig {
}
