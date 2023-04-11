package study.datajpa.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

// Spring Data JPA와는 상관없는 임의의 리파지토리
@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private EntityManager em;

    List<Member> findAllMembers (){
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
