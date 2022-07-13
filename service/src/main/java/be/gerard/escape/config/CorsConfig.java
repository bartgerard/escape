package be.gerard.escape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig
 *
 * @author bartgerard
 * @version v0.0.1
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    //@Override
    //public void addCorsMappings(final CorsRegistry registry) {
    //    registry.addMapping("/**")
    //            .allowedOrigins("*")
    //            .allowedMethods("*")
    //            .allowedHeaders("*")
    //            .maxAge(3600)
    //            .allowCredentials(true);
    //}

}
