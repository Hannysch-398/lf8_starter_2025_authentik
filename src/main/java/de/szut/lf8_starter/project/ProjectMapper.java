package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeEntity;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import org.springframework.stereotype.Service;

@Service

public class ProjectMapper {

    public ProjectEntity mapAddProjectDtoToProject(ProjectCreateDTO dto){
        ProjectEntity newSupplier = new ProjectEntity();

        return null;
    }

    public GetProjectDTO mapProjectToGetProjectDTO(ProjectEntity project) {
        GetProjectDTO dto = new GetProjectDTO();
        return dto;
    }

    public GetEmployeeDTO mapEmployeeToGetEmployeeDTO(EmployeeEntity employee){
        GetEmployeeDTO dto = new GetEmployeeDTO();
        return dto;
    }

}
