package gg.jominsubyungsin.filter;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.service.jwt.JwtService;
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
  JwtService jwtService;


  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request1 = (HttpServletRequest) request;
    String token = request1.getHeader("Authorization");
    int target_index = token.lastIndexOf("Bearer");
    int target_last = token.length();

    token = token.substring(target_index, target_last);
    UserEntity user;
    try {
      user = jwtService.accessTokenDecoding(token);
      request.setAttribute("user", user);

      chain.doFilter(request, response);
    }catch (Exception e){
      throw e;
    }

  }

}
