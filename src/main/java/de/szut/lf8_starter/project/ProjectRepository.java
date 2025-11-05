package de.szut.lf8_starter.project;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    //    boolean existsByEmIdAndResponsibleTrue(Long emId);
    boolean existsByEmployeeAssignment_EmployeeId(Long employeeId);

}
