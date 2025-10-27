package de.szut.lf8_starter.project.dto;

import de.szut.lf8_starter.project.ProjectEntity;
import lombok.Data;

import java.time.LocalDate;
@Data
public class GetProjectDTO {
    private Long id;
    private Long maId;        // Mitarbeiter-ID
    private Long kuId;        // Kunden-ID
    private String kuName;    // Kundenname
    private String projectgoal;
    private LocalDate startDate;
    private LocalDate endDate;

    public GetProjectDTO mapProjectToGetProjectDTO(ProjectEntity project) {
        GetProjectDTO dto = new GetProjectDTO();
        dto.setId(project.getId());
        dto.setMaId(project.getEmId());
        dto.setKuId(project.getCuId());
        dto.setKuName(project.getCuName());
        dto.setProjectgoal(project.getProjectgoal());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        return dto;
    }

}
