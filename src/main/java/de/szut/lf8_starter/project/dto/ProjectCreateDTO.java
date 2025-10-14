package de.szut.lf8_starter.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectCreateDTO {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Customer name must not exceed 50 characters")
    private String ku_name;

    @NotBlank(message = "Start date is mandatory")
    @Size(max = 50, message = "Start date must not exceed 50 characters")
    private String start_date;

    @NotBlank(message = "Start date is mandatory")
    @Size(max = 50, message = "Start date must not exceed 50 characters")
    private String end_date;
}
