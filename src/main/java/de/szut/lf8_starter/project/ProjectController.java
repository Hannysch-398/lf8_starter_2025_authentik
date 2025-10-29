package de.szut.lf8_starter.project;


import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.hello.HelloEntity;
import de.szut.lf8_starter.hello.dto.HelloCreateDto;
import de.szut.lf8_starter.hello.dto.HelloGetDto;
import de.szut.lf8_starter.project.dto.CreateProjectResponseDTO;
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
import java.util.Map;

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
    public ResponseEntity<CreateProjectResponseDTO> createProject(
            @Valid @RequestBody ProjectCreateDTO dto) {

//        ProjectEntity newProject = this.service.create(dto); // Service wandelt DTO in Entity um
//        final GetProjectDTO response = this.mapper.mapProjectToGetProjectDTO(newProject);
//       return new ResponseEntity<>(response, HttpStatus.CREATED);


        ProjectEntity newProject = service.create(dto);

        CreateProjectResponseDTO response = new CreateProjectResponseDTO(
                newProject.getId(),
                "created"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    public ResponseEntity<List<GetProjectDTO>> getAllProjects() {
        List<ProjectEntity> all = this.service.readAll();
        List<GetProjectDTO> dtoList = new LinkedList<>();
        for (ProjectEntity project : all) {
            dtoList.add(this.mapper.mapProjectToGetProjectDTO(project));
        }

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProjectDTO> getProjectById(@PathVariable final long id) {
        final var entity = this.service.readByID(id);
        final GetProjectDTO dto = this.mapper.mapProjectToGetProjectDTO(entity);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}/employees")
    public ResponseEntity<List<GetEmployeeDTO>> findAllEmployeesInProject(@PathVariable final long id) {

        var project = this.service.readByID(id);

        if (project == null) {
            return ResponseEntity.notFound().build();
        }


//        var employees = project.getEmployees();
//        List<GetEmployeeDTO> dtoList = new LinkedList<>();
//
//        for (EmployeeEntity employee : employees) {
//            dtoList.add(this.mapper.mapEmployeeToGetEmployeeDTO(employee));
//        }
//
//        return ResponseEntity.ok(dtoList);
//    }


        return null;
    }
}
