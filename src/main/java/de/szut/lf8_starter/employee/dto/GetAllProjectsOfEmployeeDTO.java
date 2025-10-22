package de.szut.lf8_starter.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllProjectsOfEmployeeDTO {

    private long projectId;
    private String projectName;

}
