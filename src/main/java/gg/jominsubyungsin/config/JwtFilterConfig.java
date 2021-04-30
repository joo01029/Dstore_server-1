package gg.jominsubyungsin.config;

import gg.jominsubyungsin.filter.JwtAuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

@Configuration
public class JwtFilterConfig {
  @Bean
  public FilterRegistrationBean authFilter(){
    try {
      FilterRegistrationBean registrationBean = new FilterRegistrationBean(new JwtAuthorizationFilter());
      registrationBean.addUrlPatterns("/user/*");
      registrationBean.addUrlPatterns("/project/create");
      registrationBean.addUrlPatterns("/project/detail");
      registrationBean.setOrder(2);

      return registrationBean;
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "fuck cors");
    }
  }

}
