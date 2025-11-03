package de.szut.lf8_starter.employee.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReturnGetAllProjectsOfEmployeeDTO {
    private long id;
    private List<GetAllProjectsOfEmployeeDTO> projects;

}
