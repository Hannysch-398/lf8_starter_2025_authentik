package de.szut.lf8_starter.employee.dto;

import lombok.Data;
import java.util.ArrayList;

@Data
public class GetEmployeeDTO {

    private Long em_id;
    private String lastName;
    private String firstName;
    private String postcode;
    private String city;
    private String phone;
    private ArrayList skill;
}
