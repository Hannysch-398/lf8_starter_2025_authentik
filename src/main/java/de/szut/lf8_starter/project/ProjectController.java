package de.szut.lf8_starter.project;


import de.szut.lf8_starter.project.dto.CreateProjectResponseDTO;
import de.szut.lf8_starter.employee.EmployeeMapper;
import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.project.dto.GetEmployeesInProjectDTO;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/lf8_starter/projects")

public class ProjectController {

    private final ProjectService service;
    private final ProjectMapper mapper;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public ProjectController(ProjectService service, ProjectMapper mapper, EmployeeService employeeService,
                             EmployeeMapper employeeMapper) {
        this.service = service;
        this.mapper = mapper;
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    /**
     * POST /api/projects
     * Nimmt ein ProjectRequestDto entgegen, validiert es und erstellt ein neues Projekt.
     */
    @PostMapping
    public ResponseEntity<CreateProjectResponseDTO> createProject(@Valid @RequestBody ProjectCreateDTO dto,
                                                                  @RequestHeader("Authorization")
                                                                  String authorization) {


        ProjectEntity newProject = this.service.create(dto, authorization);

        CreateProjectResponseDTO response = new CreateProjectResponseDTO(newProject.getId(), "created");

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
    public ResponseEntity<List<GetEmployeesInProjectDTO>> findAllEmployeesInProject(@PathVariable final long id,
                                                                                    @RequestHeader
                                                                                    String authorization) {
        //Projekt wird aus dem Projektservice geholt
        var project = this.service.readByID(id);

        //Es wird geguckt ob das Projekt überhaupt existiert das angefordert wurde
        if (project == null) {
            return ResponseEntity.notFound().build();
        }


        //Von dem Projekt werden die MitarbeiterIDs ausgelesesn als ArrayList
        var employeeIDs = project.getEmployeeAssignment();
        if (employeeIDs == null || employeeIDs.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        var employees = this.employeeService.getEmployees(employeeIDs, authorization);
        System.out.println(employees);

        var dtoList = employees.stream().map(employeeMapper::mapEmployeeToGetEmployeesInProjectDTO).toList();

        return new ResponseEntity<>(dtoList, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable final long id) {
        if (this.service.readByID(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //TODO: Abfrage ob Mitarbeiter noch in dem Projekt sind
        //   if(this.service.readByID(id).getEmployees()!= null){
        //     return new ResponseEntity<>(HttpStatus.CONFLICT);
        //}
        this.service.delete(id);
        return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
    }
}

