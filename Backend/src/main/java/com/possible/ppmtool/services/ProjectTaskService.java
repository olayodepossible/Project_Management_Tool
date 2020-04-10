package com.possible.ppmtool.services;

import com.possible.ppmtool.exceptions.ProjectNotFoundException;
import com.possible.ppmtool.model.Backlog;
import com.possible.ppmtool.model.ProjectTask;
import com.possible.ppmtool.repositories.BacklogRepository;
import com.possible.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        try {
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);

            projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getPriority() == 0 || projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }

            if(projectTask.getStatus() == "" || projectTask.getStatus() == null){
                projectTask.setStatus("TO_DO");
            }
            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project not Found");
        }
    }

     public List<ProjectTask> findBacklogById(String backlog_id) {


        return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_Id){
       Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);

        if(backlog == null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exit");
        }

       ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_Id);
        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task with ID: '"+pt_Id+"' not found");
        }

        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task with ID: '"+pt_Id+"' does not exit in project with backlog id '"+backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask toUpdateTask, String backlog_id, String pt_Id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,  pt_Id);
        projectTask = toUpdateTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTBySequence(String backlog_id, String pt_Id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,  pt_Id);

        projectTaskRepository.delete(projectTask);
    }
}
