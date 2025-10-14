package de.szut.lf8_starter.project;

import jakarta.persistence.Id;
import de.szut.lf8_starter.employee.EmployeeEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name= "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mitarbeiter-ID
    @Column(name = "ma_id", nullable = false)
    private Long em_Id;

    // Kunden-ID
    @Column(name = "ku_id", nullable = false)
    private Long cu_Id;

    @Column(name = "ku_name", nullable = false)
    private String cu_name;

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date; // Projektbeginn

    @Column(name = "end_date", nullable = false)
    private LocalDate end_date; // Projektende

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity emloyee;

    */
/*    @OneToMany(mappedBy = "project")
   private List<EmployeeEntity> employees;

   public List<EmployeeEntity> getEmployees(){
        return employees;
  }*/

}
