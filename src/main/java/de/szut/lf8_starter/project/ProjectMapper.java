package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import org.springframework.stereotype.Service;

@Service

public class ProjectMapper {

    public ProjectEntity mapAddProjectDtoToProject(ProjectCreateDTO dto){
        ProjectEntity newProject = new ProjectEntity();
        newProject.setEmId(dto.getMa_id());
        newProject.setCuId(dto.getKu_id());
        newProject.setCuName(dto.getKu_name());
        newProject.setProjectgoal(dto.getProjectgoal());
        newProject.setStartDate(dto.getStart_date());
        newProject.setEndDate(dto.getEnd_date());

        return newProject;
    }

    public GetProjectDTO mapProjectToGetProjectDTO(ProjectEntity entity) {
        GetProjectDTO dto = new GetProjectDTO();
        dto.setId(entity.getId());
        dto.setMaId(entity.getEmId());
        dto.setKuId(entity.getCuId());
        dto.setKuName(entity.getCuName());
        dto.setProjectgoal(entity.getProjectgoal());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        return dto;
    }

    public GetEmployeeDTO mapEmployeeToGetEmployeeDTO(EmployeeEntity employee){
        GetEmployeeDTO dto = new GetEmployeeDTO();
        return dto;
    }

}
