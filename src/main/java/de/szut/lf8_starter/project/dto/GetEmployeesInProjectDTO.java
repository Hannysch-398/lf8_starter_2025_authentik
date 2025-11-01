package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.employee.dto.SkillDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeesInProjectDTO {
    private Long emId;
    private Long skillId;
}
