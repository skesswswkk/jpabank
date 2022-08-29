package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository //@Component 내장하고 있어 컴포넌트 스캔의 대상이 되어, 자동으로 스프링 빈으로 등록
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext //스프링이 생성한 EntityManager -> em에 주입
    private final EntityManager em;

//    public MemberRepository(EntityManager em){
//        this.em = em;
//    }

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        //jpql
        return em.createQuery("select m from Member m", Member.class)
                   .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
