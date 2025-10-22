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

    // ID vom für das Projekt zuständigen Mitarbeiter
    @Column(name = "ma_id", nullable = false)
    private Long em_Id;

    // Name von dem Projekt
    @Column(name = "projectName", nullable = false)
    private String projectName;

    // Kunden-ID
    @Column(name = "ku_id", nullable = false)
    private Long cu_Id;

    @Column(name = "ku_name", nullable = false)
    private String cu_name;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date; // Projektbeginn

    @Column(name = "end_date", nullable = false)
    private LocalDate end_date; // Projektende

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
