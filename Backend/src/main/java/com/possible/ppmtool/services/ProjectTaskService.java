package com.possible.ppmtool.services;

import com.possible.ppmtool.exceptions.ProjectNotFoundException;
import com.possible.ppmtool.model.Backlog;
import com.possible.ppmtool.model.ProjectTask;
import com.possible.ppmtool.repositories.BacklogRepository;
import com.possible.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

            Backlog backlog = projectService.findProjectByProjectIdentifier(projectIdentifier, username).getBacklog();       //backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getPriority() == null || projectTask.getPriority() == 0){
                projectTask.setPriority(3);
            }

            if(projectTask.getStatus() == "" || projectTask.getStatus() == null){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);

    }

     public List<ProjectTask> findBacklogById(String backlog_id, String username) {

        projectService.findProjectByProjectIdentifier(backlog_id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_Id, String username){
        projectService.findProjectByProjectIdentifier(backlog_id, username);

       ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_Id);
        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task with ID: '"+pt_Id+"' not found");
        }

        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task with ID: '"+pt_Id+"' does not exit in project with backlog id '"+backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask toUpdateTask, String backlog_id, String pt_Id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,  pt_Id, username);
        projectTask = toUpdateTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTBySequence(String backlog_id, String pt_Id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,  pt_Id, username);

        projectTaskRepository.delete(projectTask);
    }
}
