package de.szut.lf8_starter.project;


import de.szut.lf8_starter.hello.HelloEntity;
import de.szut.lf8_starter.hello.dto.HelloCreateDto;
import de.szut.lf8_starter.hello.dto.HelloGetDto;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/lf8_starter/projects")

public class ProjectController {

    private final ProjectService service;
    private final ProjectMapper mapper;

    public ProjectController(ProjectService service, ProjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }
    /**
     * POST /api/projects
     * Nimmt ein ProjectRequestDto entgegen, validiert es und erstellt ein neues Projekt.
     */
    @PostMapping
    public ResponseEntity<ProjectCreateDTO> createProject(
            @Valid @RequestBody ProjectCreateDTO dto) {

        ProjectEntity newProject = this.mapper.mapAddProjectDtoToProject(dto);
        newProject = this.service.create(newProject);
        final GetProjectDTO response = this.mapper.mapProjectToGetProjectDTO(newProject);
        return new ResponseEntity(response, HttpStatus.CREATED);



    }


}
