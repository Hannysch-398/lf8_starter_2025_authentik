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
        newProject.setEmId(dto.getEmId());
        newProject.setCuId(dto.getCuId());
        newProject.setCuName(dto.getCuName());
        newProject.setProjectgoal(dto.getProjectgoal());
        newProject.setStartDate(dto.getStartDate());
        newProject.setEndDate(dto.getEndDate());

        return newProject;
    }

    public GetProjectDTO mapProjectToGetProjectDTO(ProjectEntity entity) {
        GetProjectDTO dto = new GetProjectDTO();
        dto.setId(entity.getId());
        dto.setEmId(entity.getEmId());
        dto.setCuId(entity.getCuId());
        dto.setCuName(entity.getCuName());
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
