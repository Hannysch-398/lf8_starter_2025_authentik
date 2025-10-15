package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.project.ProjectEntity;
import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Data
public class GetProjectDTO {
    private Long id;
    private Long emId;        // Mitarbeiter-ID
    private Long cuId;        // Kunden-ID
    private String cuName;    // Kundenname
    private String projectgoal;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> employeeIds;

    public GetProjectDTO mapProjectToGetProjectDTO(ProjectEntity project) {
        GetProjectDTO dto = new GetProjectDTO();
        dto.setId(project.getId());
        dto.setEmId(project.getEmId());
        dto.setCuId(project.getCuId());
        dto.setCuName(project.getCuName());
        dto.setProjectgoal(project.getProjectgoal());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setEmployeeIds(project.getEmployeeIds());
        return dto;
    }

}
