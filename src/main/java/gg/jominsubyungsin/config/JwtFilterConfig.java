package gg.jominsubyungsin.config;

import gg.jominsubyungsin.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class JwtFilterConfig {
  @Autowired
  private HandlerExceptionResolver handlerExceptionResolver;


  @Bean
  public FilterRegistrationBean<JwtAuthorizationFilter> authFilter(){
<<<<<<< Updated upstream
    try {
      FilterRegistrationBean<JwtAuthorizationFilter> registrationBean = new FilterRegistrationBean<>(new JwtAuthorizationFilter());
      registrationBean.setFilter(new JwtAuthorizationFilter());
      registrationBean.addUrlPatterns("/user/*");
      registrationBean.addUrlPatterns("/project/create");
      registrationBean.addUrlPatterns("/project/detail");
      registrationBean.setOrder(3);
=======
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(new JwtAuthorizationFilter());
    registrationBean.addUrlPatterns("/user/*");
    registrationBean.addUrlPatterns("/project/create");
    registrationBean.addUrlPatterns("/project/detail");
    registrationBean.setOrder(1);
>>>>>>> Stashed changes

      return registrationBean;
    }catch (Exception e){
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "fuck cors");
    }
  }

}
