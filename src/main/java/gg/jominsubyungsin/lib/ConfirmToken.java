package gg.jominsubyungsin.lib;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class ConfirmToken {
	public String removeStartString(HttpServletRequest request, String type){
		Enumeration<String> token = request.getHeaders("Authorization");
		if(token.hasMoreElements()){
			String value = token.nextElement();

			if(value.toLowerCase().startsWith(type.toLowerCase())){
				return value.substring(type.length()).trim();
			}
		}

		return null;
	}
}
