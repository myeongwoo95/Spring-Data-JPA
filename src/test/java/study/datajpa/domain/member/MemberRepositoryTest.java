package study.datajpa.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.controller.dto.member.MemberTestDto;
import study.datajpa.domain.team.Team;
import study.datajpa.domain.team.TeamRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @BeforeEach
    void beforeAll() {
        //TODO 고아객체 설정 및 테스트

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        Member member1 = Member.builder().username("memberA").age(10).team(teamA).build();
        Member member2 = Member.builder().username("memberA").age(20).team(teamB).build();

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @Test
    void methodQueryTest(){
        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 15);

        Member findMember = members.get(0);

        assertThat(findMember.getUsername()).isEqualTo("memberA");
        assertThat(findMember.getAge()).isEqualTo(20);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    void namedQueryTest(){
        List<Member> members = memberRepository.findByUsernameNamedQuery("memberA");

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void QueryTest(){
        List<Member> members = memberRepository.findByUsernameAndAgeQuery("memberA", 20);

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void columQueryTest(){
        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void dtoQueryTest(){
        List<MemberTestDto> members = memberRepository.findMemberDto();

        for (MemberTestDto member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void QueryINTest(){

        Member member1 = Member.builder().username("memberAA").age(10).build();
        Member member2 = Member.builder().username("memberBB").age(20).build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> names =  Arrays.asList(member1.getUsername(), member2.getUsername());
        List<Member> findMembers = memberRepository.findByNames(names);

        for (Member member : findMembers) {
            System.out.println("member = " + member);
            Assertions.assertThat(names).contains(member.getUsername());
        }
    }

    @Test
    void JpaReturnTypeTest() {

        Member member1 = Member.builder().username("memberAAA").age(10).build();
        Member member2 = Member.builder().username("memberBBB").age(20).build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findListByUsername("memberAAA");
        Member user = memberRepository.findOneByUsername("memberBBB"); // 2개 이상 나올 시 에러가 발생하니 유의
        Optional<Member> optionalUser = memberRepository.findOptionalByUsername("memberAAA");

        for (Member member : members) {
            System.out.println("member = " + member);
        }
        System.out.println(user);
        System.out.println(optionalUser.get());

    }

}