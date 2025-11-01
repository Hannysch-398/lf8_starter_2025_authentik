package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeAssignment;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "project")
@Getter
@Setter
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mitarbeiter-ID
    @Column(name = "emId", nullable = false)
    private Long emId;

    // Name von dem Projekt
    @Column(name = "projectName", nullable = false)
    private String projectName;

    // Kunden-ID
    @Column(name = "cuId", nullable = false)
    private Long cuId;

    @Column(name = "cuName", nullable = false)
    private String cuName;

    @Column(name = "projectgoal", nullable = false)
    private String projectgoal;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate; // Projektbeginn
    
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate; // Projektende

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;


    // Liste der Mitarbeiter-IDs
    @ElementCollection
    @CollectionTable(name = "project_employee", joinColumns = @JoinColumn(name = "project_id"))
    private List<EmployeeAssignment> employeeAssignment;


}
