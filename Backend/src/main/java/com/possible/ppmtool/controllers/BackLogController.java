package com.possible.ppmtool.controllers;

import com.possible.ppmtool.exceptions.ProjectNotFoundException;
import com.possible.ppmtool.model.ProjectTask;
import com.possible.ppmtool.services.ProjectTaskService;
import com.possible.ppmtool.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BackLogController {

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("/{backlog_id}")
    public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_id, Principal principal){
        ResponseEntity<?> errorMap = validationErrorService.MapValidationError(result);
        if(errorMap != null) return errorMap;

        ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());

        return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
    }


    @GetMapping("/{backlog_id}")
    public ResponseEntity<?> getProjectBacklog(@PathVariable String backlog_id, Principal principal){
        List<ProjectTask> projectTasks =  projectTaskService.findBacklogById(backlog_id, principal.getName());

        if(projectTasks.size() == 0){
            throw new ProjectNotFoundException("Project with ID '"+backlog_id+"' not found for this Project");
        };
        return new ResponseEntity<>(projectTasks, HttpStatus.OK) ;
    }

    @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<ProjectTask> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){
        ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id, principal.getName());
        return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
    }

    @PatchMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask,
                                               BindingResult result, @PathVariable String backlog_id, @PathVariable String pt_id, Principal principal){

        ResponseEntity<?> errorMap = validationErrorService.MapValidationError(result);
        if(errorMap != null) return errorMap;


        ProjectTask updatedProjectTask = projectTaskService.updateByProjectSequence(projectTask, backlog_id, pt_id, principal.getName());
        return new ResponseEntity<ProjectTask>(updatedProjectTask, HttpStatus.OK);
    }


    @DeleteMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<String> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id, Principal principal) {
        projectTaskService.deletePTBySequence(backlog_id, pt_id, principal.getName());

        return new ResponseEntity<String>("Project Task '"+pt_id+"' was successfully deleted", HttpStatus.OK);
    }

    }
