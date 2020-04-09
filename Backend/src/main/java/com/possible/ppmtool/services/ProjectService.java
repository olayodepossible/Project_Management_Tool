package com.possible.ppmtool.services;

import com.possible.ppmtool.exceptions.ProjectIdException;
import com.possible.ppmtool.model.Backlog;
import com.possible.ppmtool.model.Project;
import com.possible.ppmtool.repositories.BacklogRepository;
import com.possible.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdate(Project project){
         String projectIdentifier = project.getProjectIdentifier().toUpperCase();
        try{
            project.setProjectIdentifier(projectIdentifier);
             //To save as soon as Parent is been saved
            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            }
            // To avoid setting child enetity to null whenever we update the parent
            else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }
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
