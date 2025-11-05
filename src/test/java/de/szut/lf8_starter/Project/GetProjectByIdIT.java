package de.szut.lf8_starter.Project;

import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectMapper;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.project.ProjectService;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class GetProjectByIdIT {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private ProjectEntity projectEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setProjectName("Testprojekt");
        projectEntity.setCuId(5L);
        projectEntity.setCuName("Max Mustermann");
        projectEntity.setProjectgoal("Ziel");
        projectEntity.setStartDate(LocalDate.of(2025, 11, 1));
        projectEntity.setEndDate(LocalDate.of(2025, 12, 1));
    }

    @Test
    void getProjectById_returnsProject() {
        // Mock das Repository
        when(projectRepository.findById(1L)).thenReturn(Optional.of(projectEntity));

        // Optional: Mapper mocken
        GetProjectDTO dto = new GetProjectDTO();
        dto.setId(1L);
        dto.setProjectName("Testprojekt");
        when(projectMapper.mapProjectToGetProjectDTO(projectEntity)).thenReturn(dto);

        // Service aufrufen
        ProjectEntity result = projectService.readByID(1L);

        // Verify
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Testprojekt", result.getProjectName());

        verify(projectRepository, times(1)).findById(1L);
    }

}
