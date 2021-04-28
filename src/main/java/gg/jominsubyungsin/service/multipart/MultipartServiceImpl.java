package gg.jominsubyungsin.service.multipart;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

  private Path fileStorageLocation = Paths.get("static/").toAbsolutePath().normalize();

  @Value("${server.get.url}")
  String server;
  @Override
  public FileDto uploadSingle(MultipartFile file){
    if(file.isEmpty()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일이 비었음");
    }
    //파일 이름 재 생
    String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + Objects.requireNonNull(file.getOriginalFilename()));
    try{
      if(fileName.contains("..")){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 이름 오류");
      }

      //저장할 파일 위치
      Path targetLocation = fileStorageLocation.resolve(fileName);
      FileDto fileDto = new FileDto();
      String type = fileName.substring(fileName.lastIndexOf(".")+1);
      if(type.equals("jpeg") || type.equals("png")||type.equals("jpg")){
        fileDto.setType("image");
      }else if(type.equals("mp4")||type.equals("AVI")){
        fileDto.setType("video");
      }else{
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식");
      }
      //파일 생성
      File newFile = new File(targetLocation.toString());
      boolean result = newFile.createNewFile();
      //파일에 받아온 파일의 값 넣음
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


      fileDto.setFileLocation(server+"/file/see/"+fileName);
      fileDto.setType(type);
      return fileDto;
    }catch (IOException e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
  }//adsadsad

  @Override
  public List<FileDto> uploadMulti(List<MultipartFile> files){
    for(MultipartFile file:files) {
      if (file.isEmpty()) {
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일이 비었음");
      }
    }

    List<FileDto> fileDtos = new ArrayList<>();
    List<String> fileNames = new ArrayList<>();
    for(MultipartFile file:files){
      fileNames.add(StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + Objects.requireNonNull(file.getOriginalFilename())));
    }

    try{
      for(int i = 0; i < files.toArray().length; i++){
        if(fileNames.get(i).contains("..")){
          throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "파일 이름 오류");
        }
        String type = fileNames.get(i).substring(fileNames.get(i).lastIndexOf(".")+1);
        FileDto fileDto = new FileDto();

        if(type.equals("jpeg") || type.equals("png")||type.equals("jpg")){
          fileDto.setType("image");
        }else if(type.equals("mp4")||type.equals("AVI")){
          fileDto.setType("video");
        }else{
          throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식");
        }

        Path targetLocation = fileStorageLocation.resolve(fileNames.get(i));

        File newFile = new File(targetLocation.toString());
        boolean result = newFile.createNewFile();

        if(!result){
          throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 실패");
        }

        Files.copy(files.get(i).getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);



        fileDto.setType(type);
        fileNames.set(i,server+"/file/see/"+fileNames.get(i));
        fileDto.setFileLocation(fileNames.get(i));

        fileDtos.add(fileDto);
      }
      return fileDtos;
    }catch (IOException e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
  }//adsadsad
}
