package de.szut.lf8_starter.project;

import de.szut.lf8_starter.project.support.ProjectApiClient;
import de.szut.lf8_starter.project.support.ProjectTestData;
import de.szut.lf8_starter.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class GetEmployeesInProjectIT extends AbstractIntegrationTest {

    @Test
    @WithMockUser(roles = "user")
    void getEmployees_200_andDto() throws Exception {
        var api = new ProjectApiClient(mockMvc);
        long id = api.createProjectAndReturnId(ProjectTestData.validProjectJson("projekt teamy"));

        mockMvc.perform(get(ProjectTestData.BASE_URL + "/" + id + "/employees").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.employeesInProject").isArray());
    }
}
