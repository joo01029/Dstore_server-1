package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.file.response.BannerResponse;
import gg.jominsubyungsin.domain.entity.BannerEntity;
import gg.jominsubyungsin.lib.Log;
import gg.jominsubyungsin.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/file")
public class FileController {
	private final FileService fileService;
	private final Log log;

	@GetMapping("/banner/{filename}")
	public ResponseEntity<UrlResource> getBanner(@PathVariable String filename, HttpServletRequest request) {
		try {
			UrlResource resource = fileService.loadBannerFile(filename);
			String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.body(resource);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("-------------------------------------------------");
			log.error("error at GET /file/banner/{filename} controller");
			log.error("-------------------------------------------------");

			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
		} catch (Exception e) {
			log.error("-------------------------------------------------");
			log.error("error at /file/banner/{filename} controller");
			log.error("-------------------------------------------------");
			throw e;
		}
	}

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
			log.error("error at GET /file/see/{filename} controller");
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
		} catch (Exception e) {
			log.error("error at GET /file/see/{filename} controller");
			throw e;
		}
	}

	@GetMapping("/locations/banner")
	@ResponseBody
	public BannerResponse getBannerLocations() {
		BannerResponse response = new BannerResponse();
		try {
			List<BannerEntity> banners = fileService.getBannerList();
			log.info("banner first id = " + banners.get(0).getId());
			response.setBannerLocation(banners);
			response.setHttpStatus(HttpStatus.OK);
			response.setMessage("성공");
			return response;
		} catch (Exception e) {
			log.error("error at GET /file/locations/banner controller");
			throw e;
		}
	}
}
