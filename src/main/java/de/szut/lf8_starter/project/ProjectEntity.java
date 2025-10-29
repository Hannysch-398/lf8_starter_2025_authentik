package de.szut.lf8_starter.project;

import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Column(name = "project_name", nullable = false)
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

   /*
   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;
  */

/*   } @OneToMany(mappedBy = "project")
   private List<EmployeeEntity> employees;

   public List<EmployeeEntity> getEmployees(){
        return employees;
}
*/

    // Liste der Mitarbeiter-IDs
    @ElementCollection
    @CollectionTable(name = "project_employee", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "employee_id")
    private List<Long> employeeIds = new ArrayList<>();


}
