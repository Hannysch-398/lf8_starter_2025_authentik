

package de.szut.lf8_starter.employee;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@Service
public class EmployeeService {
    private final RestTemplate restTemplate;
//    @Value("${employee.service.url}")
    private String employeeServiceUrl = "https://employee-api.szut.dev/employees";

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean employeeExists(Long emId) {
        String url = employeeServiceUrl + emId;
        try {
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
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
        ResponseEntity<GetEmployeeDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GetEmployeeDTO.class
        );
        return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden");
    }
}

    public List<GetEmployeeDTO> getEmployees(List<EmployeeAssignment> employeeAssignments, String authorization) {
        return employeeAssignments.stream()
                .map(EmployeeAssignment::getEmployeeId)
                .map(id -> getEmployee(id, authorization))
                .toList();
    }


    public boolean employeeHasSkill(Long employeeId, Long skillId, String authorization) {
        String url = employeeServiceUrl + "/" + employeeId;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        if (authorization != null && !authorization.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // wir holen als String, damit wir die rohe Antwort sehen
            ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//            System.out.println("employeeHasSkill -> url: " + url);
//            System.out.println("employeeHasSkill -> status: " + resp.getStatusCode());
//            System.out.println("employeeHasSkill -> content-type: " + resp.getHeaders().getContentType());
//            System.out.println("employeeHasSkill -> raw body: " + resp.getBody());

            String body = resp.getBody();
            if (body == null || body.isBlank()) return false;

            // JSON parsen (robust)
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            // support different shapes: maybe employee object or array
            JsonNode skillSetNode = root.get("skillSet");
            if (skillSetNode == null || !skillSetNode.isArray()) {
                System.out.println("employeeHasSkill -> kein skillSet gefunden");
                return false;
            }

            // Liste aller Skill-IDs im Log sammeln
            List<String> foundIds = new ArrayList<>();
            for (JsonNode s : skillSetNode) {
                JsonNode idNode = s.get("id");
                if (idNode != null && !idNode.isNull()) {
                    String idStr = idNode.asText();
                    foundIds.add(idStr);
                }
            }
            System.out.println("employeeHasSkill -> skill IDs found: " + foundIds);

            // Vergleich tolerant: string-compare der Zahlen (handles Long/Integer/Text)
            String wanted = String.valueOf(skillId);
            boolean match = foundIds.stream().anyMatch(id -> id.equals(wanted));

            System.out.println("employeeHasSkill -> match = " + match);
            return match;

        } catch (HttpClientErrorException.NotFound nf) {
            System.err.println("employeeHasSkill -> Mitarbeiter nicht gefunden: " + employeeId);
            return false;
        } catch (Exception e) {
            System.err.println("employeeHasSkill -> Fehler beim Prüfen des Mitarbeiters " + employeeId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
