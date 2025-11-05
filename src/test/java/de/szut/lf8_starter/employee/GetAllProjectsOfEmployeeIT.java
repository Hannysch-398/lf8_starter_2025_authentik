package de.szut.lf8_starter.employee;

import de.szut.lf8_starter.employee.EmployeeTestData;
import de.szut.lf8_starter.project.support.ProjectTestData;
import de.szut.lf8_starter.project.support.ProjectApiClient;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


/**
 * Happy-Path ITs für:
 * GET /lf8_starter/employees/{employeeId}/projects
 */
public class GetAllProjectsOfEmployeeIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void returns200_andOnlyProjectsOfEmployee() throws Exception {
        var api = new ProjectApiClient(mockMvc);

        long employeeId = 42L;

        // Seed: 2 Projekte mit employeeId=42
        api.createProjectAndReturnId(EmployeeTestData.projectJsonForEmployee(employeeId, "Projekt Alpha"));
        api.createProjectAndReturnId(EmployeeTestData.projectJsonForEmployee(employeeId, "Projekt Beta"));

        // Seed: 1 Projekt mit anderem Employee (soll NICHT auftauchen)
        api.createProjectAndReturnId(EmployeeTestData.projectJsonWithoutEmployee(7L, "Fremdprojekt"));

        mockMvc.perform(get("/lf8_starter/employees/{employeeId}/projects", employeeId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) employeeId)))
                .andExpect(jsonPath("$.projects", hasSize(2)))
                .andExpect(jsonPath("$.projects[*].projectName", containsInAnyOrder("Projekt Alpha", "Projekt Beta")))
                .andExpect(jsonPath("$.projects[0].projectId").isNumber())
                .andExpect(jsonPath("$.projects[0].startDate").value(anyOf(is("2025-12-05"), is("2026-01-02")))) // ISO-8601 String
                .andExpect(jsonPath("$.projects[0].endDate").exists())
                .andExpect(jsonPath("$.projects[0].skillSet").isArray());
    }

    @Test
    @WithMockUser(roles = "user")
    void returns200_andEmptyList_whenNoProjects() throws Exception {
        long employeeId = 777L;

        // keine Seeds für 777 → leere Liste erwartet
        mockMvc.perform(get("/lf8_starter/employees/{employeeId}/projects", employeeId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) employeeId)))
                .andExpect(jsonPath("$.projects", hasSize(0)));
    }
}
