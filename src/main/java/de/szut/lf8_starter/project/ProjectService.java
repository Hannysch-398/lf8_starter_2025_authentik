package de.szut.lf8_starter.project;

import de.szut.lf8_starter.exceptionHandling.InvalidProjectDateException;
import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;

@Repository
@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final RestTemplate restTemplate;

    private static final String EMPLOYEE_SERVICE_URL = "https://employee-api.szut.dev/employees";

    public ProjectService(ProjectRepository repository, ProjectMapper mapper, RestTemplate restTemplate) {

        this.repository = repository;
        this.mapper = mapper;
        this.restTemplate = restTemplate;
    }

    public ProjectEntity create(ProjectCreateDTO dto) {
        verifyEmployeeExists(dto.getMa_id());

        if (dto.getMa_id() == null) {
            throw new InvalidProjectDateException("Employee-ID darf nicht null sein");
        }
        if (dto.getStart_date() == null || dto.getEnd_date() == null) {
            throw new InvalidProjectDateException("Start- und Enddatum müssen angegeben werden");
        }

        ProjectEntity entity = mapper.mapAddProjectDtoToProject(dto);

        //  Business-Regeln prüfen
        if (entity.getEndDate().isBefore(entity.getStartDate())) {
            throw new InvalidProjectDateException("Enddatum darf nicht vor Startdatum liegen.");
        }

        //  Speichern
        return repository.save(entity);
    }

    public List<ProjectEntity> readAll() {
        return repository.findAll();
    }

    public ProjectEntity readByID(long id) {

        Optional<ProjectEntity> oProject = repository.findById(id);

        if (oProject.isEmpty()) {
            throw new ResourceNotFoundException("Entity not found on Id = " + id);
        }

        return oProject.get();

    }
//    private void verifyEmployeeExists(Long employeeId) {
//        try {
//            restTemplate.getForObject(EMPLOYEE_SERVICE_URL + employeeId, Object.class);
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
//                throw new ResourceNotFoundException("Mitarbeiter mit ID " + employeeId + " nicht gefunden");
//            }
//            throw e; // andere HTTP-Fehler weiterwerfen
//        }
//    }
    private void verifyEmployeeExists(Long emId) {
        if (emId == null) {
            throw new IllegalArgumentException("emId darf nicht null sein");
        }
        // Dummy: Angenommen der Mitarbeiter existiert
    }
}
