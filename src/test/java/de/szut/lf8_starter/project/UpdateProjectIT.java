package de.szut.lf8_starter.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.szut.lf8_starter.project.support.ProjectApiClient;
import de.szut.lf8_starter.project.support.ProjectTestData;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UpdateProjectIT extends AbstractIntegrationTest {

    private final ObjectMapper om = new ObjectMapper();

    @Test
    @WithMockUser(roles = "user")
    void update_200_andUpdatedFields() throws Exception {
        // create
        long id = new ProjectApiClient(mockMvc)
                .createProjectAndReturnId(ProjectTestData.validProjectJson("projekt alt"));

        // update
        mockMvc.perform(put("/lf8_starter/projects/{id}", id)
                        .with(jwt())
                        .header("Authorization", "Bearer mock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ProjectTestData.validProjectJson("Projekt Update")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) id))
                .andExpect(jsonPath("$.projectName").value("Projekt Update"));
    }
}
