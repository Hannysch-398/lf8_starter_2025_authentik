package de.szut.lf8_starter.employee;

public final class EmployeeTestData {
    private EmployeeTestData() {}

    public static String projectJsonForEmployee(long employeeId, String projectName) {
        return """
        {
          "emId": %d,
          "projectName": "%s",
          "cuId": 5,
          "cuName": "Mustermann",
          "projectgoal": "hallo",
          "startDate": "2025-12-05",
          "endDate": "2025-12-20",
          "employeeAssignment": [
            {"employeeId": %d, "skillId": 1}
          ],
          "actualEndDate": "2026-01-01"
        }
        """.formatted(employeeId, projectName, employeeId);
    }

    public static String projectJsonWithoutEmployee(long otherEmployeeId, String projectName) {
        return """
        {
          "emId": %d,
          "projectName": "%s",
          "cuId": 7,
          "cuName": "AndererKunde",
          "projectgoal": "nope",
          "startDate": "2026-01-02",
          "endDate": "2026-01-31",
          "employeeAssignment": [
            {"employeeId": %d, "skillId": 2}
          ],
          "actualEndDate": "2026-02-01"
        }
        """.formatted(otherEmployeeId, projectName, otherEmployeeId);
    }
}
