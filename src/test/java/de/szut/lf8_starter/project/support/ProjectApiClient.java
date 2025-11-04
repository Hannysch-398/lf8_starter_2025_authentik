package de.szut.lf8_starter.project.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectApiClient {

    private final MockMvc mockMvc;
    private final ObjectMapper om = new ObjectMapper();

    public ProjectApiClient(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public long createProjectAndReturnId(String json) throws Exception {
        MvcResult res = mockMvc.perform(post(ProjectTestData.BASE_URL)
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_user"))) // auth ✔️
                        .header("Authorization", "Bearer mock")                           // header ✔️
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode body = om.readTree(res.getResponse().getContentAsString());
        long projectId = body.path("projectId").asLong(-1);
        if (projectId <= 0) throw new IllegalStateException("Create-Response ohne gültige projectId: " + body);
        return projectId;
    }
}
