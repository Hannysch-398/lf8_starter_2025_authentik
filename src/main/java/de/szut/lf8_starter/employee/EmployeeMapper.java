package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.GetAllProjectsOfEmployeeDTO;
import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.employee.dto.ReturnGetAllProjectsOfEmployeeDTO;
import de.szut.lf8_starter.project.ProjectEntity;
import de.szut.lf8_starter.project.dto.GetEmployeesInProjectDTO;
import de.szut.lf8_starter.project.dto.ReturnGetEmployeesInProjectDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeMapper {


    public ReturnGetEmployeesInProjectDTO mapProjectToGetEmployeesInProjectDTO(ProjectEntity project) {
/*
        Long skillId = null;
        if (employee.getSkillset() != null && !employee.getSkillset().isEmpty()) {
            skillId = employee.getSkillset().getFirst().getId(); // SkillDTO hat vermutlich getId()
        }

        GetEmployeesInProjectDTO employeesInProjectDTO =
                new GetEmployeesInProjectDTO(employee.getId(), employee.getski());
        return employeesInProjectDTO;*/

        ReturnGetEmployeesInProjectDTO dto =
                new ReturnGetEmployeesInProjectDTO(project.getId(), project.getProjectName(),
                        project.getEmployeeAssignment());

        return dto;

    }

    public ReturnGetAllProjectsOfEmployeeDTO mapProjectEntityToGetAllProjectsOfEmployeeDTO(
            List<ProjectEntity> projectsOfEmployee, long employeeId) {

        List<GetAllProjectsOfEmployeeDTO> allProjectsOfEmployee = new ArrayList<>();

        for (ProjectEntity project : projectsOfEmployee) {
            //TODO: herausfinden, wie man die skills aus einem projekt von einem mitarbeiter auslesen kann (benötigt
            // skills die hinzugefügt werden können mit id)
            allProjectsOfEmployee.add(
                    new GetAllProjectsOfEmployeeDTO(project.getId(), project.getProjectName(), project.getStartDate(),
                            project.getEndDate(), project.getEmployeeAssignment()));
        }


        return new ReturnGetAllProjectsOfEmployeeDTO(employeeId, allProjectsOfEmployee);
    }

}
