package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.employee.EmployeeAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReturnGetEmployeesInProjectDTO {
    private long id;
    private String projectName;
    private List<EmployeeAssignment> employeesInProject;
}
