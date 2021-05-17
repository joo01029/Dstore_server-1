package gg.jominsubyungsin.domain.dto.project.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
public class PutProjectDto {

	private String title;

	private String content;

	private List<Long> addUsers;

	private List<Long> rmUsers;

	private List<MultipartFile> addFiles;

	private List<Long> rmFiles;

	private List<String> addTags;

	private List<String> rmTags;
}
