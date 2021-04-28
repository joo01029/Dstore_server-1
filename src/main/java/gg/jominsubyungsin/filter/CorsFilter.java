package gg.jominsubyungsin.filter;

import lombok.val;
import org.apache.catalina.connector.Response;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.persistence.OneToMany;
import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Component
@Order(1)
public class CorsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse res = (HttpServletResponse) response;
    HttpServletRequest req = (HttpServletRequest) request;

    res.setHeader("Access-Control-Allow-Origin","*");
    res.setHeader("Access-Control-Allow-Credentials", "true");
    res.setHeader("Access-Control-Allow-Methods","POST,GET,PUT,OPTIONS,DELETE,PATCH,HEAD");
    res.setHeader("Access-Control-Max-Age","3600");
    res.setHeader("Access-Control-Allow-Headers","X-Request-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-header,Cache-Control, Pragma, Expires");
    res.setHeader("Access-Control-Expose-Headers","content-disposition");



    if (HttpMethod.OPTIONS.name().equalsIgnoreCase(req.getMethod())) {
      res.setStatus(HttpServletResponse.SC_OK);
    }

    chain.doFilter(request,response);

  }

}

