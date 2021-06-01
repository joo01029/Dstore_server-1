package gg.jominsubyungsin.lib;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RequiredArgsConstructor
@Component
public class ConfirmToken {
	private final Log log;

	public String removeStartString(HttpServletRequest request, String type) {
		try {
			Enumeration<String> token = request.getHeaders("Authorization");

			if (token.hasMoreElements()) {
				String value = token.nextElement();
				log.info("token = "+ value);
				if (value.toLowerCase().startsWith(type.toLowerCase())) {
					log.info("jwt is started at " + type);
					return value.substring(type.length()).trim();
				}
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "잘못된 토큰 타입");
			}
			log.error("jwt token is null");
			return null;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			log.error("jwt Confirm error");
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
