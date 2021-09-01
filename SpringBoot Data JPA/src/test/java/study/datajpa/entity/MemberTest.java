package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {


    @PersistenceContext
    EntityManager em;

    @Test
    public void test(){


        Team teamA= new Team("teamA");
        Team teamB= new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

       Member member1= new Member("member1", 10 ,teamA);
        Member member2= new Member("member1", 20 ,teamA);
        Member member3= new Member("member1", 30 ,teamB);
        Member member4= new Member("member1", 40 ,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        
        em.flush();   // persis해도 영속성컨텍스트에 있따 바로db에날리는게x // 이건 강제로 db 쿼리날림
        em.clear(); //영속성컨텍스트 캐쉬 날려버림


        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member: members){
            System.out.println("member="+member);
            System.out.println("->member.team="+member);
        }


    }
}