package de.szut.lf8_starter.Project;
import de.szut.lf8_starter.employee.EmployeeAssignment;
import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.BadRequestException;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.ProjectMapper;
import de.szut.lf8_starter.project.ProjectRepository;
import de.szut.lf8_starter.project.ProjectService;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProjectIT {
    @Mock
    private ProjectRepository repository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ProjectMapper mapper;

    @InjectMocks
    private ProjectService projectService;

    private ProjectCreateDTO dto;
    private ProjectEntity existingProject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Beispiel-Daten
        dto = new ProjectCreateDTO();
        dto.setEmId(1L);
        dto.setCuId(10L);
        dto.setProjectName("Neues Projekt");
        dto.setCuName("Max Mustermann");
        dto.setProjectgoal("Neues Ziel");
        dto.setStartDate(LocalDate.of(2025, 11, 5));
        dto.setEndDate(LocalDate.of(2025, 12, 15));

        List<EmployeeAssignment> assignments = new ArrayList<>();
        assignments.add(new EmployeeAssignment(1L, 101L));
        dto.setEmployeeAssignment(assignments);

        existingProject = new ProjectEntity();
        existingProject.setId(1L);
        existingProject.setProjectName("Altes Projekt");
        existingProject.setEmployeeAssignment(new ArrayList<>());
    }

    @Test
    void updateProject_shouldUpdateFields_whenValid() {
        // Mocking: Repository & EmployeeService
        when(repository.findById(1L)).thenReturn(Optional.of(existingProject));
        doNothing().when(employeeService).employeeExists(anyLong(), anyString());
        when(employeeService.employeeHasSkill(anyLong(), anyLong(), anyString())).thenReturn(true);
        when(repository.save(any(ProjectEntity.class))).thenAnswer(i -> i.getArgument(0));

        // Mock RestTemplate für Kundenprüfung
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("dummyCustomer");

        // Methode testen
        ProjectEntity updated = projectService.update(1L, dto, "dummy-token");

        // Assertions
        assertEquals(dto.getProjectName(), updated.getProjectName());
        assertEquals(dto.getProjectgoal(), updated.getProjectgoal());
        assertEquals(dto.getEmployeeAssignment().size(), updated.getEmployeeAssignment().size());

        // Sicherstellen, dass Repository.save aufgerufen wurde
        verify(repository).save(any(ProjectEntity.class));
    }

//    @Test
//    void updateProject_shouldThrowBadRequest_whenEmployeeLacksSkill() {
//        when(repository.findById(1L)).thenReturn(Optional.of(existingProject));
//        when(employeeService.employeeExists(anyLong(), anyString())).thenReturn(true);
//
//        // Skill check scheitert
//        when(employeeService.employeeHasSkill(anyLong(), anyLong(), anyString())).thenReturn(false);
//
//        // RestTemplate mocken
//        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("dummyCustomer");
//
//        BadRequestException ex = assertThrows(BadRequestException.class,
//                () -> projectService.update(1L, dto, "dummy-token"));
//
//        assertTrue(ex.getMessage().contains("has no skill"));
//    }
}


