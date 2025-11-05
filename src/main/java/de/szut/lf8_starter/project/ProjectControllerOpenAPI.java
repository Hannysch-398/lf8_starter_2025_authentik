package de.szut.lf8_starter.project;

import de.szut.lf8_starter.employee.EmployeeAssignment;
import de.szut.lf8_starter.project.dto.CreateProjectResponseDTO;
import de.szut.lf8_starter.project.dto.GetProjectDTO;
import de.szut.lf8_starter.project.dto.ProjectCreateDTO;
import de.szut.lf8_starter.project.dto.ReturnGetEmployeesInProjectDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;



    public interface ProjectControllerOpenAPI {

        @Operation(summary = "Erstellt ein neues Projekt")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Projekt erfolgreich erstellt",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = CreateProjectResponseDTO.class))}),
                @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten", content = @Content),
                @ApiResponse(responseCode = "401", description = "Nicht autorisiert", content = @Content)
        })
        ResponseEntity<CreateProjectResponseDTO> createProject(@Valid ProjectCreateDTO dto, String authorization);

        @Operation(summary = "Liefert eine Liste aller Projekte")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Liste aller Projekte",
                        content = {@Content(mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = GetProjectDTO.class)))}),
                @ApiResponse(responseCode = "401", description = "Nicht autorisiert", content = @Content)
        })
        ResponseEntity<List<GetProjectDTO>> getAllProjects();

        @Operation(summary = "Liefert ein Projekt anhand seiner ID")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Projekt gefunden",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = GetProjectDTO.class))}),
                @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden", content = @Content)
        })
        ResponseEntity<GetProjectDTO> getProjectById(long id);

        @Operation(summary = "Liefert alle Mitarbeiter, die an einem Projekt beteiligt sind")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Liste der Mitarbeiter im Projekt",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = ReturnGetEmployeesInProjectDTO.class))}),
                @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden", content = @Content)
        })
        ResponseEntity<ReturnGetEmployeesInProjectDTO> findAllEmployeesInProject(long id);

        @Operation(summary = "Löscht ein Projekt")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Projekt erfolgreich gelöscht"),
                @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden", content = @Content),
                @ApiResponse(responseCode = "409", description = "Projekt enthält noch Mitarbeiter", content = @Content)
        })
        @ResponseStatus(HttpStatus.NO_CONTENT)
        ResponseEntity<Void> delete(long id);

        @Operation(summary = "Aktualisiert ein bestehendes Projekt")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Projekt erfolgreich aktualisiert",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = GetProjectDTO.class))}),
                @ApiResponse(responseCode = "404", description = "Projekt nicht gefunden", content = @Content)
        })
        ResponseEntity<GetProjectDTO> updateProject(long id, @Valid ProjectCreateDTO dto, String authorization);

        @Operation(summary = "Entfernt einen Mitarbeiter aus einem Projekt")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Mitarbeiter erfolgreich entfernt"),
                @ApiResponse(responseCode = "404", description = "Projekt oder Mitarbeiter nicht gefunden", content = @Content)
        })
        @ResponseStatus(HttpStatus.NO_CONTENT)
        ResponseEntity<Void> removeEmployeeFromProject(long projectId, long employeeId);

        @Operation(summary = "Fügt einen Mitarbeiter einem Projekt hinzu")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Mitarbeiter erfolgreich hinzugefügt"),
                @ApiResponse(responseCode = "404", description = "Projekt oder Mitarbeiter nicht gefunden", content = @Content)
        })
        ResponseEntity<Void> addEmployeeToProject(long id, @Valid EmployeeAssignment dto, String authorization);
    }


