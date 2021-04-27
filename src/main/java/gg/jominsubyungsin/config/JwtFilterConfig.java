package gg.jominsubyungsin.config;

import gg.jominsubyungsin.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class JwtFilterConfig {
  @Autowired
  private HandlerExceptionResolver handlerExceptionResolver;

  @Bean
  public FilterRegistrationBean<JwtAuthorizationFilter> authFilter(){
    FilterRegistrationBean<JwtAuthorizationFilter> registrationBean = new FilterRegistrationBean<JwtAuthorizationFilter>();
    registrationBean.setFilter(new JwtAuthorizationFilter());
    registrationBean.addUrlPatterns("/user/*");
    registrationBean.addUrlPatterns("/project/create");
    registrationBean.setOrder(3);

    return registrationBean;
  }

}
