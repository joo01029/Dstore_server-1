package gg.jominsubyungsin.config;

import gg.jominsubyungsin.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public FilterRegistrationBean getFilterRegistrationBean(){
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(new CorsFilter());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;

  @Override
  public void addCorsMappings(CorsRegistry registry){
    registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET","POST","PUT","DELETE")
            .allowedHeaders("Content-Type","Authorization")
            .maxAge(3600);

  }


}