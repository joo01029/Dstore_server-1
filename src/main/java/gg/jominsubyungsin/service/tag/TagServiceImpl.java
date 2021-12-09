package gg.jominsubyungsin.service.tag;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.*;
import gg.jominsubyungsin.domain.repository.*;
import gg.jominsubyungsin.service.comment.CommentService;
import gg.jominsubyungsin.service.follow.FollowService;
import gg.jominsubyungsin.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {
	private final FollowService followService;
	private final LikeService likeService;
	private final CommentService commentService;

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
		} catch (Exception e){
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> TagList(String tag, Pageable pageable) {
		List<String> tagList = new ArrayList<>();
		try {
			Page<TagEntity> tags = tagListRepository.findByTagContaining(tag, pageable);
			for (TagEntity tagEntity : tags) {
				tagList.add(tagEntity.getTag());
			}
			return tagList;
		} catch (Exception e){
			throw e;
		}
	}

	/*
	 * 태그들로 프로젝트 보기
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SelectProjectDto> projectList(List<String> tags, UserEntity user, Pageable pageable) {
		List<SelectProjectDto> projects = new ArrayList<>();
		List<Long> tagEntities = new ArrayList<>();
		try {
			for (String tag : tags) {
				TagEntity tagEntity = tagRepository.findByTag(tag).orElseGet(() -> {
					throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "존재하지 않는 테그");
				});
				tagEntities.add(tagEntity.getId());
			}

			List<Long> project_ids = projectListRepository.findProjectTags(pageable, tagEntities, tagEntities.size());
			List<ProjectEntity> projectEntities = projectRepository.findAllByIdIsInOrderByIdDesc(project_ids);
			for (ProjectEntity projectEntity : projectEntities) {
				Long id = projectEntity.getId();
				List<SelectUserDto> users = new ArrayList<>();

				Long likeNum = likeService.LikeNum(id);
				LikeEntity likeState = likeService.getLikeState(projectEntity, user);
				Long commentNum = commentService.commentNum(id);

				for (ProjectUserConnectEntity connectEntity : projectEntity.getUsers()) {
					users.add(new SelectUserDto(connectEntity.getUser(), followService.followState(connectEntity.getUser(), user)));
				}

				List<String> projectTags = new ArrayList<>();
				for (ProjectTagConnectEntity connectEntity : projectEntity.getTags()) {
					projectTags.add(connectEntity.getTag().getTag());
				}
				projects.add(new SelectProjectDto(projectEntity, users, projectTags, likeNum, likeState.getState(), commentNum));
			}
			return projects;
		} catch (Exception e){
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Long projectListCount(List<String> tags) {
		List<Long> tagEntities = new ArrayList<>();
		try {
			for (String tag : tags) {
				TagEntity tagEntity = tagRepository.findByTag(tag).orElseGet(() -> {
					throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "존재하지 않는 테그");
				});
				tagEntities.add(tagEntity.getId());
			}
			return projectRepository.countProjectTags(tagEntities);
		} catch (Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public void rmProjectTagConnect(String tag, ProjectEntity project) {
		try {
			TagEntity tagEntity = tagRepository.findByTag(tag).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "존재하지 않는 테그");
			});
			projectTagConnectRepository.removeByProjectAndTag(project, tagEntity);
		} catch (Exception e){
			throw e;
		}
	}

}
