package study.datajpa.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.controller.dto.member.MemberTestDto;
import study.datajpa.domain.team.Team;
import study.datajpa.domain.team.TeamRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @PersistenceContext EntityManager em;

    @BeforeEach
    void beforeAll() {
        //TODO 고아객체 설정 및 테스트

//        Team teamA = new Team("teamA");
//        Team teamB = new Team("teamB");
//
//        Member member1 = Member.builder().username("memberA").age(10).team(teamA).build();
//        Member member2 = Member.builder().username("memberA").age(20).team(teamB).build();
//
//        teamRepository.save(teamA);
//        teamRepository.save(teamB);
//
//        memberRepository.save(member1);
//        memberRepository.save(member2);
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

    @Test
    void pagingTest(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        // 참고로 Slice<Member> 슬라이스는 전체갯수, 전체페이지를 가져올 수 없음
        assertThat(content.size()).isEqualTo(3); // 갯수
        assertThat(page.getTotalElements()).isEqualTo(5); // Page는 count 쿼리 포함하니, total 갯수도 알 수 있음
        assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지
        assertThat(page.getTotalPages()).isEqualTo(2); // 총 페이지
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void bulkUpdateTest(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 15));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 30));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);
        
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void fetchJoinTest(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        Team teamC = new Team("teamC");
        Team teamD = new Team("teamD");
        Team teamE = new Team("teamE");

        teamRepository.save(teamA);
        teamRepository.save(teamB);
        teamRepository.save(teamC);
        teamRepository.save(teamD);
        teamRepository.save(teamE);

        memberRepository.save(new Member("memberA", 10, teamA));
        memberRepository.save(new Member("memberA", 20, teamB));
        memberRepository.save(new Member("memberA", 30, teamC));
        memberRepository.save(new Member("memberA", 40, teamD));
        memberRepository.save(new Member("memberA", 50, teamE));
        
        em.flush();
        em.clear();

        System.out.println("=========================================================================================");

        //List<Member> members = memberRepository.findAll();
        //List<Member> members = memberRepository.findAllFetchJoin();
        //List<Member> members = memberRepository.findMemberEntityGraph();
        //List<Member> members = memberRepository.findByUsername("memberA");
        List<Member> members = memberRepository.findNamedEntityGraphByUsername("memberA");

        for (Member member : members) {
            System.out.println("member = " + member + ", " + "member.team().getName() = " + member.getTeam().getName()) ;
        }
    }

}