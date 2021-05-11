package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.LikeEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.TagEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.LikeRepository;
import gg.jominsubyungsin.domain.repository.ProjectListRepository;
import gg.jominsubyungsin.domain.repository.ProjectRepository;
import gg.jominsubyungsin.domain.repository.TagRepository;
import gg.jominsubyungsin.service.comment.CommentService;
import gg.jominsubyungsin.service.follow.FollowService;
import gg.jominsubyungsin.service.like.LikeService;
import gg.jominsubyungsin.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	ProjectRepository projectRepository;
	@Autowired
	ProjectListRepository projectListRepository;
	@Autowired
	LikeRepository likeRepository;
	@Autowired
	LikeService likeService;
	@Autowired
	CommentService commentService;
	@Autowired
	FollowService followService;
	@Autowired
	TagRepository tagRepository;

	/*
	프로젝트 저장
	 */
	@Override
	public void saveProject(ProjectEntity projectEntity) {
		try {
			projectRepository.save(projectEntity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	프로젝트 받아오기
	 */
	@Override
	public List<SelectProjectDto> getProjects(Pageable pageable,UserEntity me) {
		List<SelectProjectDto> projectDtos = new ArrayList<>();

		try {
			Page<ProjectEntity> projectEntityPage = projectListRepository.findAll(pageable);
			List<ProjectEntity> projectEntities = projectEntityPage.getContent();

			for (ProjectEntity projectEntity : projectEntities) {
				List<SelectUserDto> userDtos = new ArrayList<>();

				for (UserEntity userEntity : projectEntity.getUsers()) {

					SelectUserDto userDto = new SelectUserDto(userEntity,followService.followState(userEntity, me));
					userDtos.add(userDto);
				}

				SelectProjectDto selectProjectDto = new SelectProjectDto(projectEntity, userDtos);
				projectDtos.add(selectProjectDto);
			}
			return projectDtos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	유저의 프로젝트 받아오기
	 */
	@Override
	public List<SelectProjectDto> getProjects(Pageable pageable,UserEntity me, UserEntity user) {
		List<SelectProjectDto> projectDtos = new ArrayList<>();

		try {
			Page<ProjectEntity> projectEntityPage = projectListRepository.findByUsers(user, pageable);
			if (projectEntityPage.isEmpty())
				return projectDtos;

			List<ProjectEntity> projectEntities = projectEntityPage.getContent();

			for (ProjectEntity projectEntity : projectEntities) {
				System.out.println(projectEntity.getId());
				List<SelectUserDto> userDtos = new ArrayList<>();

				for (UserEntity userEntity : projectEntity.getUsers()) {
					SelectUserDto userDto = new SelectUserDto(userEntity,followService.followState(userEntity, me));
					userDtos.add(userDto);
				}

				SelectProjectDto selectProjectDetailDto = new SelectProjectDto(projectEntity, userDtos);
				projectDtos.add(selectProjectDetailDto);
			}
			return projectDtos;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
	/*
	* 프로젝트 개수
	*/
	@Override
	public Long countProject(){
		try{
			return projectRepository.count();
		}catch (Exception e){
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러");
		}
	}

	/*
	 * 유저의 프로젝트 개수
	 */
	@Override
	public Long countProject(UserEntity user){
		try{
			return projectRepository.countByUsers(user);
		}catch (Exception e){
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러");
		}
	}

	/*
	프로젝트 상세
	 */
	@Override
	public ProjectDto projectDetail(Long id, UserEntity user) {
		try {
			ProjectEntity project = projectRepository.findById(id).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});
			Long likeNum = likeService.LikeNum(id);
			LikeEntity likeState = likeRepository.findByProjectAndUser(project,user).orElse(new LikeEntity(project,user,false));
			Long commentNum = commentService.commentNum(id);

			List<SelectUserDto> users = new ArrayList<>();
			for(UserEntity userEntity: project.getUsers()){
				users.add(new SelectUserDto( userEntity,followService.followState(userEntity,user)));
			}

			return new ProjectDto(project, users, likeNum, likeState.getState(), commentNum, user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
