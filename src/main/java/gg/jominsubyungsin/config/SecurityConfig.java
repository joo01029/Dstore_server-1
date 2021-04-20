package gg.jominsubyungsin.config;

import gg.jominsubyungsin.enums.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/"
                ).hasRole(Role.USER.name())
                .antMatchers(
                        "/admin/*"
                ).hasRole(Role.ADMIN.name())
                .anyRequest().authenticated();
    }
}
