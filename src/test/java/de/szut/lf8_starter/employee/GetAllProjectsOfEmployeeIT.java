package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.GetAllProjectsOfEmployeeDTO;
import de.szut.lf8_starter.employee.dto.ReturnGetAllProjectsOfEmployeeDTO;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectMapper;
import de.szut.lf8_starter.project.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock private ProjectService projectService;
    @Mock private ProjectMapper projectMapper;
    @Mock private EmployeeService employeeService;
    @Mock private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeController employeeController;

    public EmployeeControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProjectsOfEmployee_filtersAndMaps() {
        long targetEmployeeId = 99L;

        // Projekt 1 -> anderer Mitarbeiter
        ProjectEntity project1 = new ProjectEntity();
        EmployeeAssignment a1 = new EmployeeAssignment();
        a1.setEmployeeId(1L);
        project1.setEmployeeAssignment(List.of(a1));

        // Projekt 2 -> richtiger Mitarbeiter
        ProjectEntity project2 = new ProjectEntity();
        EmployeeAssignment a2 = new EmployeeAssignment();
        a2.setEmployeeId(targetEmployeeId);
        project2.setEmployeeAssignment(List.of(a2));

        // Projekt 3 -> kein Assignment
        ProjectEntity project3 = new ProjectEntity();
        project3.setEmployeeAssignment(null);

        when(projectService.readAll()).thenReturn(List.of(project1, project2, project3));

        // ⚠️ All-Args-Constructor nutzen
        GetAllProjectsOfEmployeeDTO dto =
                new GetAllProjectsOfEmployeeDTO(
                        123L,
                        "Cooles Projekt",
                        LocalDate.of(2025, 1, 1),
                        LocalDate.of(2025, 2, 1),
                        List.of() // skillSet leer für den Test
                );

        ReturnGetAllProjectsOfEmployeeDTO expected =
                new ReturnGetAllProjectsOfEmployeeDTO(targetEmployeeId, List.of(dto));

        when(employeeMapper.mapProjectEntityToGetAllProjectsOfEmployeeDTO(
                argThat(list -> list.size() == 1 && list.get(0) == project2),
                eq(targetEmployeeId)
        )).thenReturn(expected);

        // Act
        ResponseEntity<ReturnGetAllProjectsOfEmployeeDTO> response =
                employeeController.getAllProjectsOfEmployee(targetEmployeeId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(targetEmployeeId, response.getBody().getId());
        assertEquals(1, response.getBody().getProjects().size());
        assertEquals("Cooles Projekt", response.getBody().getProjects().get(0).getProjectName());

        verify(projectService, times(1)).readAll();
        verify(employeeMapper, times(1)).mapProjectEntityToGetAllProjectsOfEmployeeDTO(
                argThat(list -> list.size() == 1 && list.get(0) == project2),
                eq(targetEmployeeId)
        );
        verifyNoMoreInteractions(projectService, employeeMapper);
        verifyNoInteractions(projectMapper, employeeService);
    }
}
