package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.support.ProjectTestData;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


public class CreateProjectIT extends AbstractIntegrationTest {

    @Test
    void create_requiresAuth() throws Exception {
        mockMvc.perform(post(ProjectTestData.BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProjectTestData.validProjectJson("projekt test")))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "user") // optional, siehe Schritt 5
    @Test
    void createProject_201() throws Exception {
        mockMvc.perform(post("/lf8_starter/projects")
                        .with(jwt()) // erzeugt gültigen Bearer + setzt Authorization-Header
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProjectTestData.validProjectJson("Projekt Alpha")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").isNumber())
                .andExpect(jsonPath("$.message").value("created"));
    }

}
