package com.possible.ppmtool.services;

import com.possible.ppmtool.exceptions.ProjectIdException;
import com.possible.ppmtool.model.Project;
import com.possible.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdate(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);

        }catch (Exception ex){
            throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier()+ "' already exists");
        }
    }

    public Project findProjectByProjectIdentifier(String projectIdentifier){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier()+ "' does not exists");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    /*public Project updateProjectByIdentifier(String projectId){
        Project projectToUpdate = projectRepository.findByProjectIdentifier(projectId);
        if(projectToUpdate == null){
            throw new ProjectIdException("Project ID '"+ projectToUpdate.getProjectIdentifier()+ "' does not exists");
        }
        projectToUpdate.setProjectIdentifier(projectToUpdate.getProjectIdentifier().toUpperCase());
        projectToUpdate.setProjectName(projectToUpdate.getProjectName());


       return projectRepository.save(projectToUpdate);
    }*/



    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Cannot delete Project ID '"+ projectId+ "'. The project does not exists");
        }
        projectRepository.delete(project);
    }
}
