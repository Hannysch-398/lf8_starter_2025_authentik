package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.*;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final RestTemplate restTemplate;

    private static final String EMPLOYEE_SERVICE_URL = "http://employee-api.szut.dev/employees";
    private static final String CUSTOMER_SERVICE_URL = "http://customer-api.szut.dev/customers";

    public ProjectService(ProjectRepository repository, ProjectMapper mapper, RestTemplate restTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    public ProjectEntity create(ProjectCreateDTO dto) {
        // Pflichtfelder prüfen
        if (dto.getEm_id() == null) {
            throw new BadRequestException("Mitarbeiter-ID darf nicht null sein");
        }
        if (dto.getCu_id() == null) {
            throw new BadRequestException("Kunden-ID darf nicht null sein");
        }
        if (dto.getCu_name() == null || dto.getCu_name().isBlank()) {
            throw new BadRequestException("Kundenansprechpartner (ku_name) darf nicht leer sein");
        }
        if (dto.getStart_date() == null || dto.getEnd_date() == null) {
            throw new BadRequestException("Start- und Enddatum müssen angegeben werden");
        }

        // Employee und Kunde prüfen
        verifyEmployeeExists(dto.getEm_id());
        verifyCustomerExists(dto.getCu_id());
        checkEmployeeAlreadyAssigned(dto.getEm_id());

        // Konflikte prüfen
//        if (isEmployeeAlreadyAssigned(dto.getEm_id())) {
//            throw new EmployeeConflictException("Mitarbeiter ist bereits in einem anderen Projekt verantwortlich");
//        }

        ProjectEntity entity = mapper.mapAddProjectDtoToProject(dto);

        // Datumsvalidierung
        if (entity.getEndDate().isBefore(entity.getStartDate())) {
            throw new InvalidProjectDateException("Enddatum darf nicht vor Startdatum liegen.");
        }
        //goal checken
        if (dto.getProjectgoal() == null || dto.getProjectgoal().isBlank()) {
            throw new BadRequestException("Projektziel darf nicht leer sein");
        }

        // Speichern
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

}