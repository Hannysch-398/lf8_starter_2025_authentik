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


//        GetEmployeesInProjectDTO employeesInProjectDTO =
//                new GetEmployeesInProjectDTO(employee.getEmId(), employee.getSkill());
//
//        return employeesInProjectDTO;
        Long skillId = null;
        if (employee.getSkillset() != null && !employee.getSkillset().isEmpty()) {
            skillId = employee.getSkillset().getFirst().getId(); // SkillDTO hat vermutlich getId()
        }

        GetEmployeesInProjectDTO employeesInProjectDTO =
                new GetEmployeesInProjectDTO(employee.getId(), employee.getSkillset());
        return employeesInProjectDTO;
    }

    public List<GetAllProjectsOfEmployeeDTO> mapProjectEntityToGetAllProjectsOfEmployeeDTO(
            List<ProjectEntity> projectsOfEmployee) {

        List<GetAllProjectsOfEmployeeDTO> allProjectsOfEmployee = new ArrayList<>();
        for (ProjectEntity project : projectsOfEmployee) {

            //TODO: herausfinden, wie man die skills aus einem projekt von einem mitarbeiter auslesen kann (benötigt
            // skills die hinzugefügt werden können mit id)
            allProjectsOfEmployee.add(
                    new GetAllProjectsOfEmployeeDTO(project.getId(), project.getProjectName(), project.getStartDate(),
                            project.getEndDate(), project.getEmployeeAssignment()));
        }

        return allProjectsOfEmployee;
    }

}
