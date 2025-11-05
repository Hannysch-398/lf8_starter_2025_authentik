package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.support.ProjectApiClient;
import de.szut.lf8_starter.project.support.ProjectTestData;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class DeleteProjectIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void delete_204_whenNoAssignments() throws Exception {
        var api = new ProjectApiClient(mockMvc);
        long id = api.createProjectAndReturnId(ProjectTestData.validProjectJson("projekt weg"));

        mockMvc.perform(delete("/lf8_starter/projects/{id}", id).with(jwt())).andExpect(status().isNoContent());
    }
    }
