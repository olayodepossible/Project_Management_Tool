package com.possible.ppmtool.services;

import com.possible.ppmtool.exceptions.ProjectIdException;
import com.possible.ppmtool.exceptions.ProjectNotFoundException;
import com.possible.ppmtool.model.Backlog;
import com.possible.ppmtool.model.Project;
import com.possible.ppmtool.model.User;
import com.possible.ppmtool.repositories.BacklogRepository;
import com.possible.ppmtool.repositories.ProjectRepository;
import com.possible.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project, String username){
         String projectIdentifier = project.getProjectIdentifier().toUpperCase();

         if(project.getId() != null){
             Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
             if(existingProject != null && (!existingProject.getProjectLeader().equals(username))){
                 throw new ProjectNotFoundException("Project not found in your account");
             }

             else if(existingProject == null ){
                 throw new ProjectNotFoundException("Project with ID '"+project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
             }
         }

        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());
            project.setProjectIdentifier(projectIdentifier);

             //To save as soon as Parent is been saved
            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            }
            // To avoid setting child entity to null whenever we update the parent
            else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }
            return projectRepository.save(project);

        }catch (Exception ex){
            throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier()+ "' already exists");
        }
    }

    public Project findProjectByProjectIdentifier(String projectIdentifier, String username){
        Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Project ID '"+ project.getProjectIdentifier()+ "' does not exists");
        }

        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findByProjectLeader(username);
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



    public void deleteProjectByIdentifier(String projectId, String username){

        projectRepository.delete(findProjectByProjectIdentifier(projectId, username));
    }
}
