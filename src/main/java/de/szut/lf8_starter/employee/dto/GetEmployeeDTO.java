package de.szut.lf8_starter.employee.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class GetEmployeeDTO {

    private Long emId;
    private String lastName;
    private String firstName;
    private String postcode;
    private String city;
    private String phone;
//    private ArrayList skill;
private List<SkillDTO> skillset;
}