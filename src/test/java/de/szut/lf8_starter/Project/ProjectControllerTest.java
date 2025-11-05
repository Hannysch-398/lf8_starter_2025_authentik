package de.szut.lf8_starter.Project;

import de.szut.lf8_starter.project.*;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectController projectController;

    public ProjectControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProjects_returnsMappedProjects() {
        //1. Mocked ProjectEntities
        ProjectEntity project1 = new ProjectEntity();
        project1.setId(1L);
        project1.setProjectName("Projekt A");
        project1.setStartDate(LocalDate.of(2025, 11, 1));
        project1.setEndDate(LocalDate.of(2025, 12, 1));

        ProjectEntity project2 = new ProjectEntity();
        project2.setId(2L);
        project2.setProjectName("Projekt B");
        project2.setStartDate(LocalDate.of(2025, 12, 1));
        project2.setEndDate(LocalDate.of(2026, 1, 1));

        List<ProjectEntity> projects = List.of(project1, project2);

        //2. Wenn service.readAll() aufgerufen wird → mock Projekte zurückgeben
        when(projectService.readAll()).thenReturn(projects);

        //3. Mapper mocks: ProjectEntity → GetProjectDTO
        GetProjectDTO dto1 = new GetProjectDTO();
        dto1.setId(1L);
        dto1.setProjectName("Projekt A");

        GetProjectDTO dto2 = new GetProjectDTO();
        dto2.setId(2L);
        dto2.setProjectName("Projekt B");

        when(projectMapper.mapProjectToGetProjectDTO(project1)).thenReturn(dto1);
        when(projectMapper.mapProjectToGetProjectDTO(project2)).thenReturn(dto2);

        //4. Controller aufrufen
        ResponseEntity<List<GetProjectDTO>> response = projectController.getAllProjects();

        //5. Prüfen
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Projekt A", response.getBody().get(0).getProjectName());
        assertEquals("Projekt B", response.getBody().get(1).getProjectName());

        //6. Verify
        verify(projectService, times(1)).readAll();
        verify(projectMapper, times(1)).mapProjectToGetProjectDTO(project1);
        verify(projectMapper, times(1)).mapProjectToGetProjectDTO(project2);
    }
}
