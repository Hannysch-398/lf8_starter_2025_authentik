package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeAssignment;
import de.szut.lf8_starter.employee.EmployeeMapper;
import de.szut.lf8_starter.project.dto.ReturnGetEmployeesInProjectDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerGetEmployeesInProjectTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectMapper projectMapper; // im Endpoint nicht genutzt, aber typischerweise im Ctor vorhanden

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private ProjectController projectController;

    public ProjectControllerGetEmployeesInProjectTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllEmployeesInProject_returns200WithMappedDto() {
        // Arrange
        long projectId = 777L;

        // Projekt mit ein paar Assignments
        ProjectEntity project = new ProjectEntity();
        project.setId(projectId);
        project.setProjectName("GenZ-Projekt");
        project.setEmployeeAssignment(List.of(
                new EmployeeAssignment(10L, 1L),
                new EmployeeAssignment(11L, 2L)
        ));

        when(projectService.readByID(projectId)).thenReturn(project);

        // Erwartete DTO-Response des Mappers
        ReturnGetEmployeesInProjectDTO expected =
                new ReturnGetEmployeesInProjectDTO(
                        projectId,
                        "GenZ-Projekt",
                        project.getEmployeeAssignment()
                );

        when(employeeMapper.mapProjectToGetEmployeesInProjectDTO(project)).thenReturn(expected);

        // Act
        ResponseEntity<ReturnGetEmployeesInProjectDTO> response =
                projectController.findAllEmployeesInProject(projectId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(projectId, response.getBody().getId());
        assertEquals("GenZ-Projekt", response.getBody().getProjectName());
        assertEquals(2, response.getBody().getEmployeesInProject().size());
        assertEquals(10L, response.getBody().getEmployeesInProject().get(0).getEmployeeId());
        assertEquals(1L, response.getBody().getEmployeesInProject().get(0).getSkillId());

        verify(projectService, times(1)).readByID(projectId);
        verify(employeeMapper, times(1)).mapProjectToGetEmployeesInProjectDTO(project);
        verifyNoMoreInteractions(projectService, employeeMapper);
        verifyNoInteractions(projectMapper);
    }

    @Test
    void findAllEmployeesInProject_returns404WhenProjectNull() {
        // Arrange
        long projectId = 888L;
        when(projectService.readByID(projectId)).thenReturn(null); // auch wenn dein Service normalerweise throwt

        // Act
        ResponseEntity<ReturnGetEmployeesInProjectDTO> response =
                projectController.findAllEmployeesInProject(projectId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(projectService, times(1)).readByID(projectId);
        verifyNoInteractions(employeeMapper, projectMapper);
    }
}
