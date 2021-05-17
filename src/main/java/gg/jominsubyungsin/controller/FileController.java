package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/file")
public class FileController {
	private final FileService fileService;

	/*
	 *파일 보기
	 */
	@GetMapping("/see/{filename}")
	public ResponseEntity<UrlResource> getImage(@PathVariable String filename, HttpServletRequest request) {
		try {
			UrlResource resource = fileService.loadFile(filename);
			String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.body(resource);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
		} catch (Exception e) {
			throw e;
		}

	}
}
