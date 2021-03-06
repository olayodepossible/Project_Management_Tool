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
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ValidationErrorService validationErrorService;

    @PostMapping("/saveProject")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){
        ResponseEntity<?> errorMap = validationErrorService.MapValidationError(result);
        if(errorMap != null)return errorMap;
        Project projectToSave = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<>(projectToSave, HttpStatus.CREATED);
    }

     @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
        Project project = projectService.findProjectByProjectIdentifier(projectId, principal.getName());

        return new ResponseEntity<Project>(project, HttpStatus.OK);
     }

     @GetMapping("/all")
    public Iterable<Project> getAllProject(Principal principal){
        return projectService.findAllProjects(principal.getName());
     }

     @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<String>("Successfully Deleted", HttpStatus.OK);
     }
}
