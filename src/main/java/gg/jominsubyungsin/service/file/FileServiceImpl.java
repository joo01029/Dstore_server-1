package gg.jominsubyungsin.service.file;

import gg.jominsubyungsin.domain.dto.file.FileDto;
import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.repository.FileRepository;
import gg.jominsubyungsin.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService{
  @Autowired
  private FileRepository fileRepository;
  @Override
  public List<FileEntity> createFile(List<FileDto> files) {
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
}
