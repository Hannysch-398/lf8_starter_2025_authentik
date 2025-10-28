package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectCreateDTO {

    @NotNull(message = "Employee-ID is mandatory")
    private Long em_id;

    @NotNull(message = "Customer-ID is mandatory")
    private Long cu_id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Customer name must not exceed 50 characters")
    private String cu_name;

    @NotBlank(message = "Projectgoal is mandatory")
    private String projectgoal;

    @NotNull(message = "Start date is mandatory")
    private LocalDate start_date;

    @NotNull(message = "End date is mandatory")
    private LocalDate end_date;
}
