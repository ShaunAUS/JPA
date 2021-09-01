package study.datajpa.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {


    @Autowired
    MemberRepository repository;

    @PersistenceContext
    EntityManager em;


    @Test
    public void test(){

        Member member = new Member("memberA");
        Member saveMember= repository.save(member);

        Member findMember = repository.findById(saveMember.getId()).get();
        
        
        //검증하기
        assertThat(findMember.getId()).isEqualTo(member.getId());  //assertions core
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }


    //페이징처리
    int age=10;
    //페이징조건  = 쿼리조건
    PageRequest pageRequest= PageRequest.of(0,3,Sort.by(Sort.Direction.DESC,"username"));
    
    //페이징조건 넣기
    Page<Member> page = repository.findByAge(age,pageRequest);

    //페이징 dto로 변환  // 엔티티를 api로 반환 x   dto로 변환후 반환 o
    Page<MemberDTO> toMap=page.map(member->new MemberDTO(member.getId(),member.getUsername(), null));


    //Slice 반환 타입
    //토탈페이지를  page처럼 가져오는게 아니라 다음페이지가 있어 없어?  리미트 1+
    //Slice<Member> page = repository.findByAge(age,pageRequest);

    //list 반환 타입
    //List<Member> page = repository.findByAge(age,pageRequest);


    //반환타입 page이면
    // 내용물 가져오기
    List<Member> content = page.getContent();
    // total count
    long totalElements=page.getTotalElements();




    //벌크 연산은  db에 바로 값 넣는다. //영속성 컨텍스트 무시한다//  영속성컨텍스트는 값 그대로... 그래서 벌크연산후 영속성컨텍스트 날려버려야함
    //벌크연산후 영속성컨텍스트 날려주자
    //안날려주면 영속성컨텍스트와 DB간의 데이터 차이발생
    @Test
    public void bulkUpdate(){

        //given // 아직 영속성 컨텍스트   // JPQL 하기전에 DB에 FLUSH 한다
        repository.save(new Member("member1",10,null));
        repository.save(new Member("member2",12,null));
        repository.save(new Member("member3",40,null));

        //when   /JPQL 은 하기전에 FLUSH 자동
        int result=repository.bulkAgePlus(20);
   /*   em.flush();
        em.clear();*/


        List<Member> result2= repository.findByUsername("member3");
        Member member3= result.get(0);

        
        //then
        assertThat(result).isEqualTo(3);

    }


    @Test
    public void findMemberLazy(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        repository.save(teamA);
        repository.save(teamB);


        Member member1 = new Member("member1", 10, temaA);
        Member member2 = new Member("member2", 10, temaB);
        repository.save(member1);
        repository.save(member2);

        em.flush();
        em.clear();

        //when  N+1문제   //  member 쿼리하나 보냈는데(1) lazy 떄문에 team 까지 불러옴(N)
        // N 이크면클수록 성능저하 - > fetch join 사용
        // lazy==  member만 나간다
        List<Member> members = repository.findAll();

        for (Member member : members) {

            System.out.println("member=" + member.getUsername());
            // team (fk) 에는 프록시라는 가짜 객체 넣고 부른다. // lazy 로딩때문에 아직 진짜 객체 안가져옴
            System.out.println("member.teamClass=" + member.getTeam();
            //team 내용 건드리는순간 진짜 데이터 가져온다// lazy // N+1 문제발생
            System.out.println("member.team=" + member.getTeam().getName());

        }
    }


    @Test
    public void queryHint(){
        //given

        Member member1 = new Member("member1", 10, null);
        repository.save(member1);
        em.flush();
        em.clear();


        //조회하는 순간에 이미 2개 (스냅샷, 원본) 생성
       Member findMember = repository.findById(member1.getId()).get();
       findMember.setUsername("member2");  //변경감지 =Dirty checking  // 스냅샷랑 기존 엔티티 2다 저장해야하므로 성능저하 유발



        //스냅샷이랑 안만든다! 변경이 안된다는 가정 // 변경감지 체크 x
        Member findMember = repository.findReadOnlyByUserName("member1");
        findMember.setUsername("member2");
        
        em.flush();


    }

    @Test
    public void Lock(){
        //given

        Member member1 = new Member("member1", 10, null);
        repository.save(member1);
        em.flush();
        em.clear();


        List<Member> result = repository.findLockByUserName("member1");

    }
}
