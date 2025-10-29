package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.*;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import org.springframework.http.HttpStatus;

import de.szut.lf8_starter.employee.EmployeeService;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.hello.HelloRepository;
import de.szut.lf8_starter.project.dto.GetEmployeesInProjectDTO;
import org.springframework.stereotype.Repository;

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


    private static final String EMPLOYEE_SERVICE_URL = "http://employee-api.szut.dev/employees";
    private static final String CUSTOMER_SERVICE_URL = "http://customer-api.szut.dev/customers";


    public ProjectService(ProjectRepository repository, ProjectMapper mapper, RestTemplate restTemplate,
                          EmployeeService employeeService) {
        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
        this.employeeService = employeeService;

    }

    public ProjectEntity create(ProjectCreateDTO dto) {
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

        // Employee und Kunde prüfen
        verifyEmployeeExists(dto.getEmId());
        verifyCustomerExists(dto.getCuId());
        checkEmployeeAlreadyAssigned(dto.getEmId());

        // Konflikte prüfen
//        if (isEmployeeAlreadyAssigned(dto.getEm_id())) {
//            throw new EmployeeConflictException("Mitarbeiter ist bereits in einem anderen Projekt verantwortlich");
//        }

        ProjectEntity entity = mapper.mapCreateProjectDtoToProject(dto);

        // Datumsvalidierung
        if (entity.getEndDate().isBefore(entity.getStartDate())) {
            throw new InvalidProjectDateException("Enddatum darf nicht vor Startdatum liegen.");
        }
        //goal checken
        if (dto.getProjectgoal() == null || dto.getProjectgoal().isBlank()) {
            throw new BadRequestException("Projektziel darf nicht leer sein");
        }

        // ProjectEntity savedEntity = repository.save(entity);


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