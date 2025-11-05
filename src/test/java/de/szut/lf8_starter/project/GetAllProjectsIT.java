package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.support.ProjectApiClient;
import de.szut.lf8_starter.project.support.ProjectTestData;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class GetAllProjectsIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void getAll_200_andList() throws Exception {
        var api = new ProjectApiClient(mockMvc);
        api.createProjectAndReturnId(ProjectTestData.validProjectJson("projekt A"));
        api.createProjectAndReturnId(ProjectTestData.validProjectJson("projekt B"));

        mockMvc.perform(get(ProjectTestData.BASE_URL).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].projectName").exists());
    }
}
