package gg.jominsubyungsin.service.multipart;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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

  private final Path fileStorageLocation = Paths.get("src/main/resources/static/").toAbsolutePath().normalize();

  @Override
  public FileDto uploadSingle(MultipartFile file){
    //파일 이름 재 생
    String fileName = StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + Objects.requireNonNull(file.getOriginalFilename()));
    try{
      if(fileName.contains("..")){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 이름 오류");
      }
      //저장할 파일 위치
      Path targetLocation = fileStorageLocation.resolve(fileName);
      //파일 생성
      File newFile = new File(targetLocation.toString());
      boolean result = newFile.createNewFile();
      //파일에 받아온 파일의 값 넣음
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      String type = fileName.substring(fileName.lastIndexOf(".")+1);
      FileDto fileDto = new FileDto();
      fileDto.setFileLocation("/static/"+fileName);
      fileDto.setType(type);
      return fileDto;
    }catch (IOException e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류");
    }
  }//adsadsad

  @Override
  public List<FileDto> uploadMulti(List<MultipartFile> files){
    List<FileDto> fileDtos = new ArrayList<>();
    List<String> fileNames = new ArrayList<>();
    for(MultipartFile file:files){
      fileNames.add(StringUtils.cleanPath(UUID.randomUUID().toString() + "-" + Objects.requireNonNull(file.getOriginalFilename())));
    }

    try{
      for(int i = 0; i < files.toArray().length; i++){
        if(fileNames.get(i).contains("..")){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "파일 이름 오류");
        }
        Path targetLocation = fileStorageLocation.resolve(fileNames.get(i));
        System.out.println(targetLocation.toString());
        File newFile = new File(targetLocation.toString());
        boolean result = newFile.createNewFile();

        Files.copy(files.get(i).getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        String type = fileNames.get(i).substring(fileNames.get(i).lastIndexOf(".")+1);
        FileDto fileDto = new FileDto();

        fileDto.setType(type);
        fileNames.set(i,"/static/"+fileNames.get(i));
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
