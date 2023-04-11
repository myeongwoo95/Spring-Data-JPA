package study.datajpa.domain.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.controller.dto.member.MemberTestDto;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    // 메서드 쿼리
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // NamedQuery : 이름 있는 쿼리로 엔티티에 쿼리를 정의하고 인터페이스에서 호출, 우선순위는 NameQuery > 메서드 쿼리
    // @Query(name = "Member.findByUsername") // 주석가능
    List<Member> findByUsernameNamedQuery(@Param("username") String username);

    // @query : 이름 없는 쿼리로 쿼리를 엔티티에 정의하지않고 바로 인터페이스에 정의할 수 있음
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findByUsernameAndAgeQuery(@Param("username") String username, @Param("age") int age);

    // @Query : 컬럼 하나 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // @Query: DTO 직접 조회
    @Query("select new study.datajpa.controller.dto.member.MemberTestDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberTestDto> findMemberDto();

    // @Query: IN절, List<String>에 포함된 이름으로 찾기
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // JPA의 다양한 반환타입
    List<Member> findListByUsername(String username); //컬렉션
    Member findOneByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건

    // 페이징
    @Query(
            value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m") // count query 분리
    Page<Member> findByAge(int age, Pageable pageable);

    // Bulk Update
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // fetch join
    @Query("select m from Member m left join fetch m.team")
    List<Member> findAllFetchJoin();

    // EntityGraph
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // JPQL + EntityGraph
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메소드 쿼리 + EntityGraph
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username);

    // NamedEntityGraph
    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(String username);

    // Hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // Lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
