package de.szut.lf8_starter.project.support;

public final class ProjectTestData {
    private ProjectTestData() {}

    public static final String BASE_URL = "/lf8_starter/projects";

    public static String validProjectJson(String name) {
        return """
        {
            "emId": 1,
            "projectName": "%s",
            "cuId": 5,
            "cuName": "Mustermann",
            "projectgoal": "hallo",
            "startDate": "2025-12-05",
            "endDate": "2025-12-20",
            "employeeAssignment": [
                {"employeeId": 1, "skillId": 1},
                {"employeeId": 2, "skillId": 1}
            ],
            "actualEndDate": "2026-01-01"
        }
        """.formatted(name);
    }
}
