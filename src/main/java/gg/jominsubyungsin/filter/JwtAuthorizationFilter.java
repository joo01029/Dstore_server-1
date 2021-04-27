package gg.jominsubyungsin.filter;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.service.jwt.JwtService;
import gg.jominsubyungsin.service.jwt.JwtServiceImpl;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthorizationFilter implements Filter {
  @Autowired
  JwtServiceImpl jwtService;

  @Override
  public void init(FilterConfig filterConfig){
    ApplicationContext ctx = WebApplicationContextUtils
            .getRequiredWebApplicationContext(filterConfig.getServletContext());

    this.jwtService = ctx.getBean(JwtServiceImpl.class);
  }
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request1 = (HttpServletRequest) request;
    String token = request1.getHeader("Authorization");

    token = token.replace("Bearer ","");

    System.out.println(token);

    try {
      UserEntity user = jwtService.accessTokenDecoding(token);
      System.out.println(user.getId());
      request.setAttribute("user", user);

      chain.doFilter(request, response);
    }catch (Exception e){
      throw e;
    }

  }

}
