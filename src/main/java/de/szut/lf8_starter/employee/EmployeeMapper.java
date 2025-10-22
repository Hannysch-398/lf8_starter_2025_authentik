package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.GetAllProjectsOfEmployeeDTO;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.dto.GetEmployeesInProjectDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeMapper {


    public GetEmployeesInProjectDTO mapEmployeeToGetEmployeesInProjectDTO(GetEmployeeDTO employee) {

        GetEmployeesInProjectDTO employeesInProjectDTO =
                new GetEmployeesInProjectDTO(employee.getEm_id(), employee.getSkill());

        return employeesInProjectDTO;
    }

    public List<GetAllProjectsOfEmployeeDTO> mapProjectEntityToGetAllProjectsOfEmployeeDTO(
            List<ProjectEntity> projectsOfEmployee) {

        List<GetAllProjectsOfEmployeeDTO> allProjectsOfEmployee = new ArrayList<>();
        for (ProjectEntity project : projectsOfEmployee) {

            allProjectsOfEmployee.add(new GetAllProjectsOfEmployeeDTO(project.getId(), project.getProjectName()));
        }

        return allProjectsOfEmployee;
    }

}
