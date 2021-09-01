package study.datajpa.repository;


import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
//순수JPA  CRUD
@Repository
public class MemberJpaRepository {

    @PersistenceContext   //
    private EntityManager em;

    public Member save(Member member){

        em.persist(member);
        return member;

    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){

        //전체조건 ,특정조건 where  == JPQL 사용!   //테이블 대상이아닌 객체를 대상으로 하는쿼리라 모양이 좀 다르다.
       return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id){
        Member member =em.find(Member.class,id);
        // member가 null일수도 잇고 아닐수도 있다
        return Optional.ofNullable(member);
    }

        //쿼리문,반환타입   //하나만 반환
    public long count(){
        return em.createQuery("select count(m) from Member m",Long.class)
        .getSingleResult();
    }
}
