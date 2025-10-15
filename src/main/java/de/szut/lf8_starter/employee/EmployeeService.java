package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class EmployeeService {
    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean employeeExists(Long em_Id) {
        String url = "http://localhost:8081/api/employees/" + em_Id;
        try {
            restTemplate.getForObject(url, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    public GetEmployeeDTO getEmployee(Long em_id) {
        String url = "http://localhost:8081/api/employees/" + em_id;
        try {
            return restTemplate.getForObject(url, GetEmployeeDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden");
        }

    }

    public List<GetEmployeeDTO> getEmployees(List<Long> employeeIds) {
        return employeeIds.stream().map(this::getEmployee).toList();
    }
}
