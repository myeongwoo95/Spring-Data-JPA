package study.datajpa.domain.member;

import lombok.*;
import study.datajpa.domain.JpaBaseEntity;
import study.datajpa.domain.team.Team;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@Entity
@NamedQuery(
        name = "Member.findByUsernameNamedQuery",
        query= "select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends JpaBaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Member(String username, int age, Team team){
        this.username = username;
        this.age = age;
        if(team != null){
            this.team = team;
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team newTeam){
        // 기존의 팀에서 해당 Member 제거
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }

        // 새로운 팀으로 변경
        this.team = newTeam;

        // 새로운 팀에서 해당 Member 추가
        if (newTeam != null) {
            newTeam.getMembers().add(this);
        }
    }
}
