package gg.jominsubyungsin.service.tag;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.*;
import gg.jominsubyungsin.domain.repository.*;
import gg.jominsubyungsin.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {
	private final FollowService followService;

	private final TagRepository tagRepository;
	private final TagListRepository tagListRepository;
	private final ProjectListRepository projectListRepository;
	private final ProjectRepository projectRepository;
	private final ProjectTagConnectRepository projectTagConnectRepository;

	@Override
	@Transactional
	public List<TagEntity> createTag(List<String> tags) {
		try {
			List<TagEntity> tagEntities = new ArrayList<>();
			for (String tag : tags) {
				TagEntity tagEntity = tagRepository.findByTag(tag).orElseGet(() -> {
					TagEntity tagEntity1 = new TagEntity(tag);
					tagRepository.save(tagEntity1);
					return tagEntity1;
				});
				tagEntities.add(tagEntity);
			}
			return tagEntities;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public List<String> TagList(String tag, Pageable pageable) {
		List<String> tagList = new ArrayList<>();
		try {
			Page<TagEntity> tags = tagListRepository.findByTagContaining(tag, pageable);
			for (TagEntity tagEntity : tags) {
				tagList.add(tagEntity.getTag());
			}
			return tagList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	/*
	 * 태그들로 프로젝트 보기
	 */
	@Override
	public List<SelectProjectDto> projectList(List<String> tags, UserEntity user, Pageable pageable) {
		List<SelectProjectDto> projects = new ArrayList<>();
		List<Long> tagEntities = new ArrayList<>();
		try {
			for (String tag : tags) {
				TagEntity tagEntity = tagRepository.findByTag(tag).orElseGet(() -> {
					throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 테그");
				});
				tagEntities.add(tagEntity.getId());
			}

			List<Long> project_ids = projectListRepository.findProjectTags(pageable, tagEntities, tagEntities.size());
			List<ProjectEntity> projectEntities = projectRepository.findAllByIdIsInOrderByIdDesc(project_ids);
			for (ProjectEntity projectEntity : projectEntities) {
				List<SelectUserDto> users = new ArrayList<>();
				for (ProjectUserConnectEntity connectEntity : projectEntity.getUsers()) {
					users.add(new SelectUserDto(connectEntity.getUser(), followService.followState(connectEntity.getUser(), user)));
				}

				List<String> projectTags = new ArrayList<>();
				for (ProjectTagConnectEntity connectEntity : projectEntity.getTags()) {
					projectTags.add(connectEntity.getTag().getTag());
				}
				projects.add(new SelectProjectDto(projectEntity, users, projectTags));
			}
			return projects;
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public Long projectListCount(List<String> tags) {
		List<Long> tagEntities = new ArrayList<>();
		try {
			for (String tag : tags) {
				TagEntity tagEntity = tagRepository.findByTag(tag).orElseGet(() -> {
					throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 테그");
				});
				tagEntities.add(tagEntity.getId());
			}
			return projectRepository.countprojectTags(tagEntities, tagEntities.size());
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void rmProjectTagConnect(String tag, ProjectEntity project) {
		try {
			TagEntity tagEntity = tagRepository.findByTag(tag).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "존재하지 않는 테그");
			});
			projectTagConnectRepository.removeByProjectAndTag(project, tagEntity);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

}
