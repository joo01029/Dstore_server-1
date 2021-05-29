package gg.jominsubyungsin.filter;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.handler.GlobalExeptionHandler;
import gg.jominsubyungsin.lib.ConfirmToken;
import gg.jominsubyungsin.service.jwt.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;


@Component
@Order(2)
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {

	private HandlerExceptionResolver handlerExceptionResolver;
	private JwtServiceImpl jwtService;
	private ConfirmToken confirmToken;

	public JwtAuthorizationFilter(HandlerExceptionResolver handlerExceptionResolver){
		this.handlerExceptionResolver = handlerExceptionResolver;
	}
	@Override
	public void init(FilterConfig filterConfig) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(filterConfig.getServletContext());

		this.jwtService = ctx.getBean(JwtServiceImpl.class);
		this.confirmToken = ctx.getBean(ConfirmToken.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request1 = (HttpServletRequest) request;
		if (!request1.getMethod().equals("OPTIONS")) {

			try {
				String token = confirmToken.removeStartString(request1, "Bearer");

				if (token == null) {
					request1.setAttribute("user", null);
					chain.doFilter(request, response);
					return;
				}
				UserEntity user = jwtService.accessTokenDecoding(token);
				request1.setAttribute("user", user);
				chain.doFilter(request, response);
			} catch (HttpClientErrorException e) {
				handlerExceptionResolver.resolveException((HttpServletRequest) request, (HttpServletResponse) response, null, e);
			}
		}

	}

}
