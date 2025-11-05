package de.szut.lf8_starter.Project;
import org.junit.jupiter.api.Test;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.project.ProjectService;
//import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class DeleteProjectIT {
    @Mock
    private ProjectRepository repository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deleteProject_existingProject_shouldCallRepositoryDelete() {
        // Arrange
        ProjectEntity project = new ProjectEntity();
        project.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(project));

        // Act
        projectService.delete(1L);

        // Assert
        verify(repository, times(1)).delete(project);
    }
}
