package gg.jominsubyungsin.service.file;

import gg.jominsubyungsin.domain.dto.file.request.FileDto;
import gg.jominsubyungsin.domain.entity.BannerEntity;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.repository.BannerRepository;
import gg.jominsubyungsin.domain.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
	private final Path fileStorageLocation = Paths.get("static/").toAbsolutePath().normalize();
	private final Path bannerFileStorageLocation = Paths.get("static/banner/").toAbsolutePath().normalize();
	private final FileRepository fileRepository;
	private final BannerRepository bannerRepository;
	@Override
	@Transactional
	public List<FileEntity> createFiles(List<FileDto> files) {
		boolean thumnail = false;
		List<FileEntity> fileEntities = new ArrayList<>();

		for (FileDto file : files) {
			if (!thumnail) {
				file.setThumnail(true);
				thumnail = true;
			} else {
				file.setThumnail(false);
			}

			file.setFileLocation(file.getFileLocation());
			file.setType(file.getType());

			FileEntity fileEntity = file.toEntity();
			try {
				fileRepository.save(fileEntity);
			} catch (Exception e) {
				e.printStackTrace();
				throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러");
			}
			fileEntities.add(fileEntity);
		}
		return fileEntities;
	}

	@Override
	public UrlResource loadFile(String filename) {

		try {
			Path file = fileStorageLocation.resolve(filename).normalize();
			UrlResource resource = new UrlResource(file.toUri());

			if (!resource.exists())
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 파일");

			return resource;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 파일");
		}
	}

	@Override
	public UrlResource loadBannerFile(String filename) {
		try {
			Path file = bannerFileStorageLocation.resolve(filename).normalize();
			UrlResource resource = new UrlResource(file.toUri());

			if (!resource.exists())
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 파일");

			return resource;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 파일");
		}
	}

	@Override
	public FileEntity findFileByProject(Long id, ProjectEntity project) {
		try {
			return fileRepository.findByIdAndProjectId(id, project).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 파일");
			});
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void rmFile(Long id) {
		try {
			fileRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public ProjectEntity addFile(ProjectEntity project, List<FileDto> files, Boolean thumnail) {
		try {
			for (FileDto file : files) {
				file.setThumnail(false);
				FileEntity fileEntity = file.toEntity();
				project.add(fileEntity);
				fileRepository.save(fileEntity);
			}
			if (!thumnail) {
				project.getFiles().get(0).setThumnail(true);
			}

			return project;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public List<BannerEntity> getBannerList(){
		try{
			return bannerRepository.findAll();
		}catch (Exception e){
			throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "서버 에러");
		}
	}
}
