package gg.jominsubyungsin.service.multipart;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MultipartServiceImpl implements MultipartService{

  private final Path fileStorageLocation = Paths.get("src/main/resources/static/").toAbsolutePath().normalize();

  @Override
  public String uploadSingle(MultipartFile file){
    String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + Objects.requireNonNull(file.getOriginalFilename()));
    System.out.println(fileName);
    try{
      if(fileName.contains("..")){
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일 이름 오류");
      }
      Path targetLocation = fileStorageLocation.resolve(fileName);
      System.out.println(targetLocation.toString());
      File newFile = new File(targetLocation.toString());
      boolean result = newFile.createNewFile();

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return "/static/"+fileName;
    }catch (IOException e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
  }//adsadsad

  @Override
  public List<String> uploadMulti(List<MultipartFile> files){
    List<String> fileNames = new ArrayList<>();
    for(MultipartFile file:files){
      fileNames.add(StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + Objects.requireNonNull(file.getOriginalFilename())));
    }

    try{
      for(int i = 0; i < files.toArray().length; i++){
        if(fileNames.get(i).contains("..")){
          throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일 이름 오류");
        }
        Path targetLocation = fileStorageLocation.resolve(fileNames.get(i));
        System.out.println(targetLocation.toString());
        File newFile = new File(targetLocation.toString());
        boolean result = newFile.createNewFile();

        Files.copy(files.get(i).getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        fileNames.set(i,"/static/"+fileNames.get(i));
      }


      return fileNames;
    }catch (IOException e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
  }//adsadsad
}
