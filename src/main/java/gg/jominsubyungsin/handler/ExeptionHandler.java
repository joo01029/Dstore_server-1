package gg.jominsubyungsin.handler;

import gg.jominsubyungsin.domain.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class ExeptionHandler {
	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<Response> ServerErrorReturn(HttpServerErrorException e) {
		Response data = new Response();
		data.setHttpStatus(e.getStatusCode());
		data.setMessage(e.getMessage());

		return new ResponseEntity<Response>(data, e.getStatusCode());
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Response> ClientErrorReturn(HttpClientErrorException e) {
		Response data = new Response();
		data.setHttpStatus(e.getStatusCode());
		data.setMessage(e.getMessage());

		return new ResponseEntity<Response>(data, e.getStatusCode());
	}
}
