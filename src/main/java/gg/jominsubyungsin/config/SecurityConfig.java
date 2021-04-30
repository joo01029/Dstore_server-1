package gg.jominsubyungsin.config;

import gg.jominsubyungsin.enums.Role;
import gg.jominsubyungsin.filter.CorsFilter;
import gg.jominsubyungsin.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;

public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	JwtAuthorizationFilter jwtAuthorizationFilter;
	@Autowired
	CorsFilter corsFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.cors().and()
				.csrf().disable()
				.headers().frameOptions().disable()
				.and()
				.cors().and()
				.authorizeRequests()
				.antMatchers("/*")
				.anonymous()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.antMatchers(
						"/"
				).hasRole(Role.USER.name())
				.antMatchers(
						"/admin/*"
				).hasRole(Role.ADMIN.name())
				.anyRequest().authenticated();
	}

}
