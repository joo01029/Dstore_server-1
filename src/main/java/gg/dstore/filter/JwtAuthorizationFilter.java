package gg.dstore.filter;

import gg.dstore.domain.entity.UserEntity;
import gg.dstore.lib.ConfirmToken;
import gg.dstore.lib.Log;
import gg.dstore.service.jwt.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {
	private HandlerExceptionResolver handlerExceptionResolver;
	private JwtServiceImpl jwtService;
	private ConfirmToken confirmToken;
	private Log log;

	public JwtAuthorizationFilter(HandlerExceptionResolver handlerExceptionResolver) {
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	public void init(FilterConfig filterConfig) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(filterConfig.getServletContext());

		this.jwtService = ctx.getBean(JwtServiceImpl.class);
		this.confirmToken = ctx.getBean(ConfirmToken.class);
		this.log = ctx.getBean(Log.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request1 = (HttpServletRequest) request;
		if (!request1.getMethod().equals("OPTIONS")) {
			try {
				log.info("jwtFilter");
				String token = confirmToken.removeStartString(request1, "Bearer");
				log.info(token);
				if (token == null) {
					request1.setAttribute("user", null);
					chain.doFilter(request, response);
					return;
				}
				UserEntity user = jwtService.accessTokenDecoding(token);
				request1.setAttribute("user", user);
				chain.doFilter(request, response);
			} catch (Exception e) {
				handlerExceptionResolver.resolveException((HttpServletRequest) request, (HttpServletResponse) response, null, e);
			}
		}

	}

}
