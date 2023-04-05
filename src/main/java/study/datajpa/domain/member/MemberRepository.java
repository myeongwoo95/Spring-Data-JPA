package study.datajpa.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // NamedQuery
    // @Query(name = "Member.findByUsername") // 주석해도 JPA는 Entity에서 NamedQuery를 먼저 찾음, 그 다음이 메서드 쿼리
    List<Member> findByusername(@Param("username") String username);

    // @query
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
