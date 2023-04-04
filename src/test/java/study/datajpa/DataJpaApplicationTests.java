package study.datajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.member.Member;
import study.datajpa.domain.member.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class DataJpaApplicationTests {

	@Autowired
	MemberRepository memberRepository;

	@Test
	void testMember() {
//		Member member = new Member("memberA", 10, );
//		Member saveMemeber = memberRepository.save(member);
//
//		Member findMember = memberRepository.findById(saveMemeber.getId()).get();
//
//		assertThat(findMember.getId()).isEqualTo(member.getId());
//		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//		assertThat(findMember).isEqualTo(member);
	}

}
