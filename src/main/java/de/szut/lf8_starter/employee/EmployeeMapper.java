package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.dto.GetEmployeeDTO;
import de.szut.lf8_starter.project.dto.GetEmployeesInProjectDTO;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {


    public GetEmployeesInProjectDTO mapEmployeeToGetEmployeesInProjectDTO(GetEmployeeDTO employee) {

        GetEmployeesInProjectDTO employeesInProjectDTO =
                new GetEmployeesInProjectDTO(employee.getEm_id(), employee.getSkill());

        return employeesInProjectDTO;
    }

}
