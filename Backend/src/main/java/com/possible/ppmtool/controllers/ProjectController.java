package com.possible.ppmtool.controllers;

import com.possible.ppmtool.model.Project;
import com.possible.ppmtool.services.ProjectService;
import com.possible.ppmtool.services.ValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("/saveProject")
    public ResponseEntity<?> createProject(@Valid @RequestBody Project project, BindingResult result){
        ResponseEntity<?> errorMap = validationErrorService.MapValidationError(result);
        if(errorMap != null)return errorMap;
        Project projectToSave = projectService.saveOrUpdate(project);
        return new ResponseEntity<>(projectToSave, HttpStatus.CREATED);
    }

     @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId){
        Project project = projectService.findProjectByProjectIdentifier(projectId);

        return new ResponseEntity<Project>(project, HttpStatus.OK);
     }

     @GetMapping("/all")
    public Iterable<Project> getAllProject(){
        return projectService.findAllProjects();
     }

     @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId){
        projectService.deleteProjectByIdentifier(projectId);
        return new ResponseEntity<String>("Successfully Deleted", HttpStatus.OK);
     }
}
