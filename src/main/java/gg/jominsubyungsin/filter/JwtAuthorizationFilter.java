package gg.jominsubyungsin.filter;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.lib.ConfirmToken;
import gg.jominsubyungsin.service.jwt.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

@Component
@Order(2)
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {
	private JwtServiceImpl jwtService;
	private ConfirmToken confirmToken;
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
			String token = confirmToken.removeStartString(request1, "Bearer ");
			System.out.println(token);
			if (token == null) {
				System.out.println("토큰이 비었음");
				request1.setAttribute("user", null);
				chain.doFilter(request, response);
				return;
			}
			try {
				UserEntity user = jwtService.accessTokenDecoding(token);
				request1.setAttribute("user", user);
				chain.doFilter(request, response);
			} catch (Exception e) {
				throw e;
			}
		}

	}

}
