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
        // Pflichtfelder prüfen
        if (dto.getEmId() == null) {
            throw new BadRequestException("Mitarbeiter-ID darf nicht null sein");
        }
        if (dto.getCuId() == null) {
            throw new BadRequestException("Kunden-ID darf nicht null sein");
        }
        if (dto.getCuName() == null || dto.getCuName().isBlank()) {
            throw new BadRequestException("Kundenansprechpartner (ku_name) darf nicht leer sein");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new BadRequestException("Start- und Enddatum müssen angegeben werden");
        }
        if (dto.getProjectName() == null) {
            throw new BadRequestException("Projektname muss angegeben werden");
        }

//        boolean hasSkill = employeeService.employeeHasSkill(a.getEmployeeId(), a.getSkillId(), authorization);

        // Employee und Kunde prüfen
        verifyEmployeeExists(dto.getEmId());
        verifyCustomerExists(dto.getCuId());
        checkEmployeeAlreadyAssigned(dto.getEmId());



        ProjectEntity entity = mapper.mapCreateProjectDtoToProject(dto);

        // Datumsvalidierung
        if (entity.getEndDate().isBefore(entity.getStartDate())) {
            throw new InvalidProjectDateException("Enddatum darf nicht vor Startdatum liegen.");
        }

        if (dto.getEmployeeAssignment() != null && !dto.getEmployeeAssignment().isEmpty()) {
            List<EmployeeAssignment> assignments = dto.getEmployeeAssignment().stream().map(a -> {
                boolean hasSkill = employeeService.employeeHasSkill(a.getEmployeeId(), a.getSkillId(), authorization);
                if (!hasSkill) {
                    throw new BadRequestException(
                            "Mitarbeiter " + a.getEmployeeId() + " besitzt Skill " + a.getSkillId() + " nicht.");
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
            throw new BadRequestException("Projektziel darf nicht leer sein");
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
            throw new ResourceNotFoundException("Projekt mit ID " + id + " nicht gefunden");
        }
        return oProject.get();
    }

    private void verifyEmployeeExists(Long emId) {
        if (emId == null) {
            throw new BadRequestException("Mitarbeiter-ID darf nicht null sein");
        }
        // Optional: RestTemplate-Aufruf prüfen
        // Dummy: Angenommen, Mitarbeiter existiert
    }

    private void checkEmployeeAlreadyAssigned(Long emId) {
        if (repository.existsByEmId(emId)) {
            throw new EmployeeConflictException("Mitarbeiter ist bereits in einem anderen Projekt eingesetzt");
        }
    }

    private void verifyCustomerExists(Long customerId) {
        if (customerId == null) {
            throw new BadRequestException("Kunden-ID darf nicht null sein");
        }
        try {
            restTemplate.getForObject(CUSTOMER_SERVICE_URL + "/" + customerId, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Kunde mit ID " + customerId + " nicht gefunden");
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
            throw new ResourceNotFoundException("Projekt mit ID " + id + " nicht gefunden");
        }

        // Pflichtfelder prüfen (wie bei create)
        if (dto.getEmId() == null) {
            throw new BadRequestException("Mitarbeiter-ID darf nicht null sein");
        }
        if (dto.getCuId() == null) {
            throw new BadRequestException("Kunden-ID darf nicht null sein");
        }
        if (dto.getCuName() == null || dto.getCuName().isBlank()) {
            throw new BadRequestException("Kundenansprechpartner darf nicht leer sein");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            throw new BadRequestException("Start- und Enddatum müssen angegeben werden");
        }
        if (dto.getProjectName() == null || dto.getProjectName().isBlank()) {
            throw new BadRequestException("Projektname darf nicht leer sein");
        }
        if (dto.getProjectgoal() == null || dto.getProjectgoal().isBlank()) {
            throw new BadRequestException("Projektziel darf nicht leer sein");
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
                    throw new BadRequestException("Mitarbeiter " + a.getEmployeeId() +
                            " besitzt Skill " + a.getSkillId() + " nicht.");
                }
            });

            existing.setEmployeeAssignment(dto.getEmployeeAssignment());
        }

        // Datum prüfen
        if (existing.getEndDate().isBefore(existing.getStartDate())) {
            throw new InvalidProjectDateException("Enddatum darf nicht vor Startdatum liegen.");
        }
        return this.repository.save(existing);
    }

}




