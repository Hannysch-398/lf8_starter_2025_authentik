package de.szut.lf8_starter.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class GetAllProjectsOfEmployeeDTO {


    private long projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String skill;

}
