package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeAssignment;
import de.szut.lf8_starter.employee.dto.SkillDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final EmployeeService employeeService;

    private final ProjectMapper mapper;
    private final RestTemplate restTemplate;

    private static final String CUSTOMER_SERVICE_URL = "http://customer-api.szut.dev/customers";
    private static final String EMPLOYEES_SERVICE_URL = "http://customer-api.szut.dev/employees";


    public ProjectService(ProjectRepository repository, ProjectMapper mapper, RestTemplate restTemplate,
                          EmployeeService employeeService) {
        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
        this.employeeService = employeeService;

    }

    public ProjectEntity create(ProjectCreateDTO dto, String authorization) {


        // Employee und Kunde prüfen
        //verifyEmployeeExists(dto.getEmId());
        employeeService.employeeExists(dto.getEmId(),authorization);
        verifyCustomerExists(dto.getCuId());
//        checkEmployeeAlreadyAssigned(dto.getEmId());


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
                checkEmployeeAvailability(a.getEmployeeId(), dto.getStartDate(), dto.getEndDate(), null);

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

    public ProjectEntity addEmployee(long id, EmployeeAssignment dto, String authorization) {

        ProjectEntity originalProject = readByID(id);
        employeeService.employeeExists(dto.getEmployeeId(),authorization);
        employeeService.employeeHasSkill(dto.getEmployeeId(), dto.getSkillId(), authorization);

        List<EmployeeAssignment> currentEmployees = originalProject.getEmployeeAssignment();

        currentEmployees.add(dto);
        originalProject.setEmployeeAssignment(currentEmployees);

        return repository.save(originalProject);
    }

    public List<ProjectEntity> readAll() {
        return repository.findAll();
    }

    public ProjectEntity readByID(long id) {
        Optional<ProjectEntity> oProject = repository.findById(id);
        if (oProject.isEmpty()) {
            throw new ResourceNotFoundException("Project with ID " + id + " not found.");
        }
        return oProject.get();
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

        // Employee und Kunde prüfen
        employeeService.employeeExists(dto.getEmId(),authorization);
        verifyCustomerExists(dto.getCuId());

        // Felder aktualisieren
        existing.setProjectName(dto.getProjectName());
        existing.setCuId(dto.getCuId());
        existing.setCuName(dto.getCuName());
        existing.setProjectgoal(dto.getProjectgoal());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setActualEndDate(dto.getActualEndDate());


        if (dto.getEmployeeAssignment() != null) {
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Employee for this project found.");
        }

        boolean removed = assignments.removeIf(a -> a.getEmployeeId().equals(employeeId));

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee " + employeeId + " is not part of this project.");
        }

        project.setEmployeeAssignment(assignments);
        repository.save(project);
    }


    private void checkEmployeeAvailability(Long emId, LocalDate startDate, LocalDate endDate, Long projectId) {
        List<ProjectEntity> overlappingProjects = repository.findAll().stream()
                .filter(p -> !p.getId().equals(projectId))
                .filter(p -> p.getEmployeeAssignment().stream()
                        .anyMatch(a -> a.getEmployeeId().equals(emId)))
                .filter(p -> !(p.getEndDate().isBefore(startDate) || p.getStartDate().isAfter(endDate)))
                .toList();

        if (!overlappingProjects.isEmpty()) {
            throw new EmployeeConflictException("Employee " + emId + " is already assigned to another project in this period.");
        }
    }

}




