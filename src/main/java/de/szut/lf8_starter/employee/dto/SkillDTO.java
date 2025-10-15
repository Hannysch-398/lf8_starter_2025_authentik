package de.szut.lf8_starter.employee.dto;

public class SkillDTO {
    private Long id;
    private String skill;

    public SkillDTO(Long id, String skill) {
        this.id = id;
        this.skill = skill;
    }

    public Long getId() {
        return id;
    }

    public String getSkill() {
        return skill;
    }
}
