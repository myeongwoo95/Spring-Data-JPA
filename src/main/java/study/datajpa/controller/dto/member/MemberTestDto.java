package study.datajpa.controller.dto.member;

import lombok.Data;

@Data
public class MemberTestDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberTestDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
}
