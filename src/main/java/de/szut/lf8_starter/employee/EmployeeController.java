package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.GetAllProjectsOfEmployeeDTO;
import de.szut.lf8_starter.employee.dto.ReturnGetAllProjectsOfEmployeeDTO;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectMapper;
import de.szut.lf8_starter.project.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lf8_starter/employees")
public class EmployeeController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(ProjectService projectService, ProjectMapper projectMapper,
                              EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping("/{employeeId}/projects")
    public ResponseEntity<ReturnGetAllProjectsOfEmployeeDTO> getAllProjectsOfEmployee(@PathVariable long employeeId) {

        //alle Projekte holen
        List<ProjectEntity> allProjects = this.projectService.readAll();

        //alle Projekte auf Mitarbeiter filtern
        List<ProjectEntity> projectOfEmployee = new ArrayList<>();
        for (ProjectEntity project : allProjects) {
            var assignments = project.getEmployeeAssignment();
            if (assignments != null) {
                for (var assignment : assignments) {
                    if (assignment.getEmployeeId() == employeeId) {
                        projectOfEmployee.add(project);
                        break; // optional: sobald gefunden, nicht weiter loopen
                    }
                }
            }
        }
        var dtoList = employeeMapper.mapProjectEntityToGetAllProjectsOfEmployeeDTO(projectOfEmployee, employeeId);
        return ResponseEntity.ok(dtoList);


    }

}
