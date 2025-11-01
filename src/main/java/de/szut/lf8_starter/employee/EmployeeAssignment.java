package de.szut.lf8_starter.employee;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
//@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmployeeAssignment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    private Long employeeId;
    private Long skillId;

}
