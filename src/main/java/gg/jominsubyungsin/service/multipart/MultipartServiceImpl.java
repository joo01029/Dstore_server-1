package gg.jominsubyungsin.service.multipart;

import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
public class MultipartServiceImpl implements MultipartService{

  private final Path fileStorageLocation = Paths.get("static/").toAbsolutePath().normalize();

  @Override
  public String uploadSingle(MultipartFile file) throws Exception{
    String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + Objects.requireNonNull(file.getOriginalFilename()));
    try{
      if(fileName.contains("..")){
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일 이름 오류");
      }
      Path targetLocation = fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return fileName;
    }catch (IOException e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
  }//aa

}
