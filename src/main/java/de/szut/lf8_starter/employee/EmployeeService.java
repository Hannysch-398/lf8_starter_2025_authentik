package de.szut.lf8_starter.employee;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    private final RestTemplate restTemplate;
    //    @Value("${employee.service.url}")
    private static final String EMPLOYEE_SERVICE_URL = "https://employee-api.szut.dev/employees";
    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);


    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void employeeExists(Long emId, String authorization) {
        String url = EMPLOYEE_SERVICE_URL + "/" + emId;

        HttpHeaders headers = new HttpHeaders();
        // Authorization-Header nur setzen, wenn vorhanden
        if (authorization != null && !authorization.isBlank()) {
            headers.add(HttpHeaders.AUTHORIZATION, authorization);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // Request an Employee-Service mit Token
            restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden (ID: " + emId + ")");
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Ungültiges oder fehlendes Token für Employee-Service");
        } catch (HttpClientErrorException.Forbidden e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Zugriff auf Employee-Service verweigert");
        } catch (HttpClientErrorException ex) {
            // Alle anderen HTTP-Fehler → 502
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Employee-Service nicht erreichbar: " + ex.getStatusCode());
        } catch (Exception ex) {
            // Sonstige Fehler (z. B. Timeout, Parsing etc.)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fehler beim Abrufen des Mitarbeiters",
                    ex);
        }
    }

//
//    public GetEmployeeDTO getEmployee(Long emId) {
//        String url = employeeServiceUrl + emId;
//        try {
//            return restTemplate.getForObject(url, GetEmployeeDTO.class);
//        } catch (HttpClientErrorException.NotFound e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden");
//        }
//
//    }

    //    public List<GetEmployeeDTO> getEmployees(List<EmployeeAssignment> employeeIds) {
//        return employeeIds.stream().map(this::getEmployee).toList();
//    }
    public GetEmployeeDTO getEmployee(Long emId, String authorization) {
        String url = "https://employee-api.szut.dev/employees/" + emId;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authorization); // <-- Token aus Controller durchreichen

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GetEmployeeDTO> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, GetEmployeeDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden");

        }
    }

    public List<GetEmployeeDTO> getEmployees(List<EmployeeAssignment> employeeAssignments, String authorization) {
        return employeeAssignments.stream().map(EmployeeAssignment::getEmployeeId)

                .map(id -> getEmployee(id, authorization)).toList();
    }


    public boolean employeeHasSkill(Long employeeId, Long skillId, String authorization) {
        String url = EMPLOYEE_SERVICE_URL + "/" + employeeId;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        if (authorization != null && !authorization.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            String body = resp.getBody();
            if (body == null || body.isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Leere Antwort vom Employee-Service");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            JsonNode skillSetNode = root.get("skillSet");
            if (skillSetNode == null || !skillSetNode.isArray()) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Kein SkillSet im Employee gefunden");
            }

            List<String> foundIds = new ArrayList<>();
            for (JsonNode s : skillSetNode) {
                JsonNode idNode = s.get("id");
                if (idNode != null && !idNode.isNull()) {
                    foundIds.add(idNode.asText());
                }
            }

            String wanted = String.valueOf(skillId);
            boolean match = foundIds.stream().anyMatch(id -> id.equals(wanted));

            if (!match) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Mitarbeiter hat den Skill " + skillId + " nicht");
            }

            return true;

        } catch (HttpClientErrorException.NotFound nf) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden: " + employeeId, nf);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fehler beim Parsen der Employee-Daten",
                    e);
        }
    }


}
