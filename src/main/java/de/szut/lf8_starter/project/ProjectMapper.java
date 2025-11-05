package de.szut.lf8_starter.project;


import de.szut.lf8_starter.employee.EmployeeAssignment;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMapper {


    public ProjectEntity mapCreateProjectDtoToProject(ProjectCreateDTO dto){
        ProjectEntity newProject = new ProjectEntity();
        newProject.setEmId(dto.getEmId());
        newProject.setProjectName(dto.getProjectName());
        newProject.setCuId(dto.getCuId());
        newProject.setCuName(dto.getCuName());
        newProject.setProjectgoal(dto.getProjectgoal());
        newProject.setStartDate(dto.getStartDate());
        newProject.setEndDate(dto.getEndDate());
        newProject.setActualEndDate(dto.getActualEndDate());
        newProject.setEmployeeAssignment(dto.getEmployeeAssignment());

        return newProject;
    }

    public GetProjectDTO mapProjectToGetProjectDTO(ProjectEntity entity) {
        GetProjectDTO dto = new GetProjectDTO();
        dto.setId(entity.getId());
        dto.setProjectName(entity.getProjectName());
        dto.setEmId(entity.getEmId());
        dto.setCuId(entity.getCuId());
        dto.setCuName(entity.getCuName());
        dto.setProjectgoal(entity.getProjectgoal());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setActualEndDate(entity.getActualEndDate());
        dto.setEmployeeAssignment(entity.getEmployeeAssignment());

        return dto;
    }

}
