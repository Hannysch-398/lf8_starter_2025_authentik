package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.dto.CreateProjectResponseDTO;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerCreateProjectTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectController projectController;

    public ProjectControllerCreateProjectTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_returns201AndResponseBody() {
        // given
        String authorization = "Bearer some-token";

        ProjectCreateDTO dto = new ProjectCreateDTO();
        dto.setEmId(1L);
        dto.setCuId(10L);
        dto.setProjectName("Neues Projekt");
        dto.setCuName("Testkunde");
        dto.setProjectgoal("Implementierung eines Systems");
        dto.setStartDate(LocalDate.of(2025, 1, 1));
        dto.setEndDate(LocalDate.of(2025, 2, 1));
        dto.setEmployeeAssignment(List.of());

        ProjectEntity savedEntity = new ProjectEntity();
        savedEntity.setId(123L);

        when(projectService.create(dto, authorization)).thenReturn(savedEntity);

        // when
        ResponseEntity<CreateProjectResponseDTO> response =
                projectController.createProject(dto, authorization);

        // then
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(123L, response.getBody().getProjectId());
        assertEquals("created", response.getBody().getMessage());

        verify(projectService, times(1)).create(dto, authorization);
        verifyNoMoreInteractions(projectService);
        verifyNoInteractions(projectMapper);
    }
}
