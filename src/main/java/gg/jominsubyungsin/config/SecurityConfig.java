package gg.jominsubyungsin.config;

import gg.jominsubyungsin.enums.Role;
import gg.jominsubyungsin.filter.CorsFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
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
