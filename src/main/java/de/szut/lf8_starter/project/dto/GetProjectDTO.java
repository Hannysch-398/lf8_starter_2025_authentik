
package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.employee.EmployeeAssignment;
import lombok.Data;

import java.util.List;
import java.time.LocalDate;

@Data
public class GetProjectDTO {
    private Long id;
    private Long emId;
    private String projectName;
    private Long cuId;
    private String cuName;
    private String projectgoal;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualEndDate;
    private List<EmployeeAssignment> employeeAssignment;


}

