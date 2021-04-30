package gg.jominsubyungsin.service.file;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.FileRepository;
import gg.jominsubyungsin.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class FileServiceImpl implements FileService{
  private final Path fileStorageLocation = Paths.get("static/").toAbsolutePath().normalize();

  @Autowired
  FileRepository fileRepository;
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public void setProfileImage(FileDto file, String email) {
    try{
      UserEntity user = userRepository.findByEmail(email).orElseGet(()->{
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지않는 이메일");
      });
      user.setProfileImage(file.getFileLocation());

      userRepository.save(user);
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러");
    }
  }

  @Override
  @Transactional
  public List<FileEntity> createFiles(List<FileDto> files) {
    boolean thumnail  = false;
    List<FileEntity> fileEntities = new ArrayList<>();

    for(FileDto file: files){
      if(!thumnail){
        file.setThumnail(1);
        thumnail = true;
      }else{
        file.setThumnail(0);
      }

      file.setFileLocation(file.getFileLocation());
      file.setType(file.getType());

      FileEntity fileEntity = file.toEntity();
      try{
        fileRepository.save(fileEntity);
      }catch (Exception e){
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

      if(!resource.exists())
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 파일");

      return resource;
    } catch (MalformedURLException e) {
      e.printStackTrace();
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 파일");
    }
  }


}
