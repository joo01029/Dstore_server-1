package gg.dstore.config;

import gg.dstore.filter.JwtAuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class JwtFilterConfig {
	private HandlerExceptionResolver handlerExceptionResolver;

	public JwtFilterConfig(HandlerExceptionResolver handlerExceptionResolver) {
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Bean
	public FilterRegistrationBean authFilter() {
		try {
			FilterRegistrationBean registrationBean = new FilterRegistrationBean(new JwtAuthorizationFilter(handlerExceptionResolver));
			registrationBean.addUrlPatterns("/user/*");
			registrationBean.addUrlPatterns("/project/*");
			registrationBean.addUrlPatterns("/comment/*");
			registrationBean.addUrlPatterns("/like/*");
			registrationBean.addUrlPatterns("/follow/*");
			registrationBean.addUrlPatterns("/tag/find/projects");
			registrationBean.setOrder(2);

			return registrationBean;
		} catch (Exception e) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "fuck cors");
		}
	}

}
