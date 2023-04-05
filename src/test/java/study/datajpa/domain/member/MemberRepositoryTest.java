package study.datajpa.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void methodQueryTest(){
        Member member1 = Member.builder().username("memberA").age(10).build();
        Member member2 = Member.builder().username("memberA").age(20).build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 15);

        Member findMember = members.get(0);

        assertThat(findMember.getUsername()).isEqualTo("memberA");
        assertThat(findMember.getAge()).isEqualTo(20);
        assertThat(members.size()).isEqualTo(1);
    }

}