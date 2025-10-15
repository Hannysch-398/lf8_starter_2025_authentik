package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.employee.dto.SkillDTO;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
public class GetEmployeesInProjectDTO {
    private Long em_id;
    private List<SkillDTO> skillSet;
}
