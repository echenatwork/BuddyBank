package web.controller.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import db.entity.AccountToInterestRateSchedule;
import db.entity.InterestRateSchedule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import web.model.AccountToScheduleBean;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        //registry.addViewController("/greeting").setViewName("greeting");
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    public Mapper mapper() {
        Mapper mapper = DozerBeanMapperBuilder.create()
                .withClassLoader(this.getClass().getClassLoader()) // need to set class loader explicitly otherwise we get type mismatch exceptions
                .withMappingFiles("config/dozer_mappings.xml").build();
        return mapper;
    }
}
