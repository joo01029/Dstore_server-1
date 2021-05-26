package gg.jominsubyungsin.service.project;

import gg.jominsubyungsin.domain.dto.file.request.FileDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.ProjectDto;
import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.project.request.GetProjectDto;
import gg.jominsubyungsin.domain.dto.project.request.PutProjectDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.*;
import gg.jominsubyungsin.domain.repository.*;

import gg.jominsubyungsin.enums.Leader;

import gg.jominsubyungsin.service.comment.CommentService;
import gg.jominsubyungsin.service.file.FileService;
import gg.jominsubyungsin.service.follow.FollowService;
import gg.jominsubyungsin.service.like.LikeService;
import gg.jominsubyungsin.service.multipart.MultipartService;
import gg.jominsubyungsin.service.tag.TagService;
import gg.jominsubyungsin.service.user.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
	private final UserService userService;
	private final LikeService likeService;
	private final FollowService followService;
	private final CommentService commentService;
	private final MultipartService multipartService;
	private final FileService fileService;
	private final TagService tagService;

	private final ProjectRepository projectRepository;
	private final ProjectListRepository projectListRepository;
	private final ProjectUserConnectRepository projectUserConnectRepository;
	private final ProjectTagConnectRepository projectTagConnectRepository;
	private final CommentRepository commentRepository;
	/*
	프로젝트 저장
	 */
	@Override
	@Transactional
	public void saveProject(GetProjectDto projectDto, UserEntity mainUser) {
		List<UserEntity> userEntities = new ArrayList<>();
		try {
			userEntities.add(mainUser);

			for (Long id : projectDto.getUsers()) {
				UserEntity saveUser = userService.findUserById(id);
				for (UserEntity compare : userEntities) {
					if (saveUser.equals(compare)) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "같은 유저 입니다");
					}
				}
				userEntities.add(saveUser);
			}

			List<FileDto> files = multipartService.uploadMulti(projectDto.getFiles());
			List<FileEntity> fileEntities = fileService.createFiles(files);

			List<TagEntity> tags = tagService.createTag(projectDto.getTags());

			ProjectEntity projectEntity = projectDto.toEntity(fileEntities);

			projectRepository.save(projectEntity);
			boolean setLeader = false;

			for (UserEntity user : userEntities) {
				Leader role = !setLeader ? Leader.LEADER : Leader.MEMBER;
				ProjectUserConnectEntity projectUserConnectEntity = new ProjectUserConnectEntity(projectEntity, user, role);
				projectUserConnectRepository.save(projectUserConnectEntity);
				setLeader = true;

				user.add(projectUserConnectEntity);
				projectEntity.addUsers(projectUserConnectEntity);
				projectUserConnectRepository.save(projectUserConnectEntity);
			}

			for (TagEntity tag : tags) {
				ProjectTagConnectEntity projectTagConnectEntity = new ProjectTagConnectEntity(projectEntity, tag);

				tag.add(projectTagConnectEntity);
				projectEntity.addTags(projectTagConnectEntity);
				projectTagConnectRepository.save(projectTagConnectEntity);
			}

		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	프로젝트 받아오기
	 */
	@Override
	public List<SelectProjectDto> getProjects(Pageable pageable, UserEntity me) {
		List<SelectProjectDto> projectDtos = new ArrayList<>();

		try {
			Page<ProjectEntity> projectEntityPage = projectListRepository.findAllByOnDeleteOrderByIdDesc(false, pageable);
			List<ProjectEntity> projectEntities = projectEntityPage.getContent();

			for (ProjectEntity projectEntity : projectEntities) {
				List<SelectUserDto> userDtos = new ArrayList<>();

				for (ProjectUserConnectEntity connectEntity : projectEntity.getUsers()) {

					if(!connectEntity.getGetOut()) {
						SelectUserDto userDto = new SelectUserDto(connectEntity.getUser(), followService.followState(connectEntity.getUser(), me));
						userDtos.add(userDto);
					}
				}
				List<String> tags = new ArrayList<>();
				for (ProjectTagConnectEntity connectEntity : projectEntity.getTags()) {
					tags.add(connectEntity.getTag().getTag());
				}

				SelectProjectDto selectProjectDto = new SelectProjectDto(projectEntity, userDtos, tags);
				projectDtos.add(selectProjectDto);
			}
			return projectDtos;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	유저의 프로젝트 받아오기
	 */
	@Override
	public List<SelectProjectDto> getProjects(Pageable pageable, UserEntity me, UserEntity user) {
		List<SelectProjectDto> projectDtos = new ArrayList<>();

		try {
			Page<ProjectEntity> projectEntityPage = projectListRepository.findByUsersAndOnDeleteOrderByIdDesc(user, false, pageable);
			if (projectEntityPage.isEmpty())
				return projectDtos;

			List<ProjectEntity> projectEntities = projectEntityPage.getContent();

			for (ProjectEntity projectEntity : projectEntities) {
				System.out.println(projectEntity.getId());
				List<SelectUserDto> userDtos = new ArrayList<>();

				for (ProjectUserConnectEntity connectEntity : projectEntity.getUsers()) {
					if(!connectEntity.getGetOut()) {
						SelectUserDto userDto = new SelectUserDto(connectEntity.getUser(), followService.followState(connectEntity.getUser(), me));
						userDtos.add(userDto);
					}
				}
				List<String> tags = new ArrayList<>();
				for (ProjectTagConnectEntity connectEntity : projectEntity.getTags()) {
					tags.add(connectEntity.getTag().getTag());
				}

				SelectProjectDto selectProjectDetailDto = new SelectProjectDto(projectEntity, userDtos, tags);
				projectDtos.add(selectProjectDetailDto);
			}
			return projectDtos;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 * 프로젝트 개수
	 */
	@Override
	public Long countProject() {
		try {
			return projectRepository.countByOnDelete(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 * 유저의 프로젝트 개수
	 */
	@Override
	public Long countProject(UserEntity user) {
		try {
			return projectRepository.countByUsersAndOnDelete(user, false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	프로젝트 상세
	 */
	@Override
	public ProjectDto projectDetail(Long id, UserEntity user) {
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});
			Long likeNum = likeService.LikeNum(id);
			LikeEntity likeState = likeService.getLikeState(project, user);
			Long commentNum = commentService.commentNum(id);

			List<SelectUserDto> users = new ArrayList<>();
			for (ProjectUserConnectEntity connectEntity : project.getUsers()) {
				users.add(new SelectUserDto(connectEntity.getUser(), followService.followState(connectEntity.getUser(), user)));
			}
			List<String> tags = new ArrayList<>();
			for (ProjectTagConnectEntity connectEntity : project.getTags()) {
				tags.add(connectEntity.getTag().getTag());
			}

			return new ProjectDto(project, users, likeNum, likeState.getState(), commentNum, tags);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void projectUpdate(Long id, UserEntity user, PutProjectDto putProjectDto) {
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글");
			});

			ProjectUserConnectEntity connectEntity = projectUserConnectRepository.findByProjectAndUserAndRole(project, user, Leader.LEADER).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "수정 권한이 없음");
			});

			int existFileSize = project.getFiles().size();
			int rmFileSize = putProjectDto.getRmFiles() == null ? 0 : putProjectDto.getRmFiles().size();
			int addFileSize = putProjectDto.getAddFiles() == null ? 0 : putProjectDto.getAddFiles().size();
			if (existFileSize - rmFileSize + addFileSize == 0) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "사진은 무조건 1개 이상 필요함");
			}

			//파일 삭제
			boolean mainPhoto = true;
			if (rmFileSize != 0) {
				for (Long fileId : putProjectDto.getRmFiles()) {
					FileEntity file = fileService.findFileByProject(fileId, project);
					if (file.getThumnail()) {
						mainPhoto = false;
					}
					project.getFiles().remove(file);
					fileService.rmFile(fileId);
				}
			}

			//파일 추가
			if (addFileSize != 0) {
				List<FileDto> files = multipartService.uploadMulti(putProjectDto.getAddFiles());
				project = fileService.addFile(project, files, mainPhoto);
			}
			//제목 수정
			String title = putProjectDto.getTitle();
			project.setTitle((null != title && !title.equals("")) ? title : project.getTitle());
			//내용 수정
			String connent = putProjectDto.getContent();
			project.setContent((connent != null && !connent.equals("")) ? connent : project.getContent());

			//태그 삭제

			if (putProjectDto.getRmTags() != null) {
				for (String rmTag : putProjectDto.getRmTags()) {
					tagService.rmProjectTagConnect(rmTag, project);
				}
			}


			if (putProjectDto.getAddUsers() != null) {
				for (Long userId : putProjectDto.getAddUsers()) {
					UserEntity addUser = userService.findUserById(userId);
					Optional<ProjectUserConnectEntity> userExist = projectUserConnectRepository.findByProjectAndUser(project, addUser);
					if (userExist.isPresent()) {
						throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 존재하는 유저");
					}
					ProjectUserConnectEntity userConnect = new ProjectUserConnectEntity(project, user, Leader.MEMBER);
					addUser.add(userConnect);
					project.addUsers(userConnect);
					projectUserConnectRepository.save(userConnect);
				}
			}
			if (putProjectDto.getAddTags() != null) {
				List<TagEntity> tags = tagService.createTag(putProjectDto.getAddTags());
				for (TagEntity tag : tags) {
					Optional<ProjectTagConnectEntity> tagExist = projectTagConnectRepository.findByProjectAndTag(project, tag);
					if (tagExist.isPresent()) {
						throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 추가한 태그");
					}

					ProjectTagConnectEntity tagConnect = new ProjectTagConnectEntity(project, tag);
					tag.add(tagConnect);
					project.addTags(tagConnect);
					projectTagConnectRepository.save(tagConnect);
				}
			}

		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void deleteProject(Long id, UserEntity user) {
		try {
			ProjectEntity project = projectRepository.findByIdAndOnDelete(id, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "존재하지 않는 프로젝트");
			});
			ProjectUserConnectEntity connect = projectUserConnectRepository.findByProjectAndUser(project, user).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이 유저의 포르젝트가 아님");
			});

			if (connect.getRole() != Leader.LEADER) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이프로젝트 리더가 아님");
			}

			project.setOnDelete(true);

			for(CommentEntity comment: project.getComments()){
				comment.setOnDelete(true);
				commentRepository.save(comment);
			}

			for(LikeEntity like: project.getLikes()){
				likeService.setLikeFalse(like);
			}
			for(ProjectUserConnectEntity userConnect: project.getUsers()){
				userConnect.setGetOut(true);
				projectUserConnectRepository.save(userConnect);
			}
			project.setOnDelete(true);
			projectRepository.save(project);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

}
