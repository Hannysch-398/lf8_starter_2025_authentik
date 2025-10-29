package de.szut.lf8_starter.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateProjectResponseDTO {
    private Long projectId;
    private String message;
}
