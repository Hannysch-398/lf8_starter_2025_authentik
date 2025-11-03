package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeAssignment;
import de.szut.lf8_starter.exceptionHandling.*;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final EmployeeService employeeService;

    private final ProjectMapper mapper;
    private final RestTemplate restTemplate;

    private static final String CUSTOMER_SERVICE_URL = "http://customer-api.szut.dev/customers";


    public ProjectService(ProjectRepository repository, ProjectMapper mapper, RestTemplate restTemplate,
                          EmployeeService employeeService) {
        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
        this.employeeService = employeeService;

    }

    public ProjectEntity create(ProjectCreateDTO dto, String authorization) {


        // Employee und Kunde prüfen
        verifyEmployeeExists(dto.getEmId());
        verifyCustomerExists(dto.getCuId());
        checkEmployeeAlreadyAssigned(dto.getEmId());



        ProjectEntity entity = mapper.mapCreateProjectDtoToProject(dto);

        // Datumsvalidierung
        if (entity.getEndDate().isBefore(entity.getStartDate())) {
            throw new InvalidProjectDateException("End date cannot be before start date.");
        }

        if (dto.getEmployeeAssignment() != null && !dto.getEmployeeAssignment().isEmpty()) {
            List<EmployeeAssignment> assignments = dto.getEmployeeAssignment().stream().map(a -> {
                boolean hasSkill = employeeService.employeeHasSkill(a.getEmployeeId(), a.getSkillId(), authorization);
                if (!hasSkill) {
                    throw new BadRequestException(
                            "Employee " + a.getEmployeeId() + " has no skill" + a.getSkillId());
                }
                return new EmployeeAssignment(a.getEmployeeId(), a.getSkillId());
            }).toList();
            dto.getEmployeeAssignment().forEach(a -> {
                System.out.println("Employee " + a.getEmployeeId() + " Skill " + a.getSkillId());
            });
            entity.setEmployeeAssignment(assignments);
        }

        //goal checken
        if (dto.getProjectgoal() == null || dto.getProjectgoal().isBlank()) {
            throw new BadRequestException("Project goal must not be empty.");
        }

       entity.setActualEndDate(dto.getActualEndDate());

        return repository.save(entity);
    }

    public List<ProjectEntity> readAll() {
        return repository.findAll();
    }

    public ProjectEntity readByID(long id) {
        Optional<ProjectEntity> oProject = repository.findById(id);
        if (oProject.isEmpty()) {
            throw new ResourceNotFoundException("End date cannot be before start date.");
        }
        return oProject.get();
    }

    private void verifyEmployeeExists(Long emId) {
        if (emId == null) {
            throw new BadRequestException("Employee ID must not be null.");
        }
        // Optional: RestTemplate-Aufruf prüfen
        // Dummy: Angenommen, Mitarbeiter existiert
    }

    private void checkEmployeeAlreadyAssigned(Long emId) {
        if (repository.existsByEmId(emId)) {
            throw new EmployeeConflictException("Employee is already assigned to another project.");
        }
    }

    private void verifyCustomerExists(Long customerId) {
        if (customerId == null) {
            throw new BadRequestException("Customer-ID must not be null.");
        }
        try {
            restTemplate.getForObject(CUSTOMER_SERVICE_URL + "/" + customerId, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Kunde mit ID " + customerId + " not found");
            }
            throw e;
        }
    }

    public void delete(long id) {
        ProjectEntity entityToDelete = readByID(id);
        repository.delete(entityToDelete);
    }
    public ProjectEntity update(long id, ProjectCreateDTO dto, String authorization) {
        ProjectEntity existing = this.readByID(id);

        if (existing == null) {
            throw new ResourceNotFoundException("ProjectID " + id + " not found.");
        }

        // Pflichtfelder prüfen (wie bei create)
        if (dto.getEmId() == null) {
            throw new BadRequestException("Employee ID must not be null.");
        }
        if (dto.getCuId() == null) {
            throw new BadRequestException("CustomerID must not be null.");
        }
        if (dto.getCuName() == null || dto.getCuName().isBlank()) {
            throw new BadRequestException("Customer contact name must not be empty.");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new BadRequestException("Start and end dates must be provided.");
        }
        if (dto.getProjectName() == null || dto.getProjectName().isBlank()) {
            throw new BadRequestException("Project name must be provided.");
        }
        if (dto.getProjectgoal() == null || dto.getProjectgoal().isBlank()) {
            throw new BadRequestException("Project goal must not be null.");
        }

        // Employee und Kunde prüfen
        verifyEmployeeExists(dto.getEmId());
        verifyCustomerExists(dto.getCuId());

        // Felder aktualisieren
        existing.setProjectName(dto.getProjectName());
        existing.setCuId(dto.getCuId());
        existing.setCuName(dto.getCuName());
        existing.setProjectgoal(dto.getProjectgoal());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setActualEndDate(dto.getActualEndDate()); // falls du das neue Feld hast

        // Optional: Mitarbeiter neu zuordnen (wenn DTO eine neue Liste hat)
        if (dto.getEmployeeAssignment() != null) {
            // Optional: Validierung (wie beim Create)
            dto.getEmployeeAssignment().forEach(a -> {
                boolean hasSkill = employeeService.employeeHasSkill(a.getEmployeeId(), a.getSkillId(), authorization);
                if (!hasSkill) {
                    throw new BadRequestException("Employee " + a.getEmployeeId() +
                            " has no skill " + a.getSkillId() );
                }
            });

            existing.setEmployeeAssignment(dto.getEmployeeAssignment());
        }

        // Datum prüfen
        if (existing.getEndDate().isBefore(existing.getStartDate())) {
            throw new InvalidProjectDateException("End date cannot be before start date.");
        }
        return this.repository.save(existing);
    }
    public void removeEmployeeFromProject(long projectId, long employeeId) {
        ProjectEntity project = repository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found."));

        List<EmployeeAssignment> assignments = project.getEmployeeAssignment();
        if (assignments == null || assignments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Keine Mitarbeiter im Projekt gefunden.");
        }

        boolean removed = assignments.removeIf(a -> a.getEmployeeId().equals(employeeId));

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee " + employeeId + " is not part of this project.");
        }

        project.setEmployeeAssignment(assignments);
        repository.save(project);
    }

}




