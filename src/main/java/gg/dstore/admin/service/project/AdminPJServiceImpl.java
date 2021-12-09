package gg.dstore.admin.service.project;

import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminPJServiceImpl implements AdminPJService {
    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectEntity> getProjectAll(){
        List<ProjectEntity> pjList;

        try {
            pjList = projectRepository.findAll();
            return pjList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
//
//    @Override
//    public List<ProjectEntity> getProjectById(Long id){
//        List<ProjectEntity> pjList;
//
//        try {
//            pjList = projectRepository.findByUsers(id);
//            return pjList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    @Override
    public List<ProjectEntity> dropProject(Long id) {
        List<ProjectEntity> pjList;

        try {
            projectRepository.deleteById(id);
            pjList = projectRepository.findAll();
            return pjList;
        } catch (Exception e) {
            throw e;
        }
    }
}
