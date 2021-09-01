package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    
    // List , optional , 단독 반환타입 지원
    
    //검색결과 dto 반환하지않고 바로 dto에 넣어버리기 // memeber의 team 부르면 그객체가 온다   fk-> pk
    @Query("select new study.datajpa.dto.MemberDTO(m.id,m.username,t.name)from Member m join m.team t")
    List<MemberDTO> findMemberDTO();


    //in절로 여러개 조회할때
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);


    //결과  없어도 empty list 반환해준다    null체크 필요없다!!! null이 절대아님!
    //결과 단건은 없으면 null이다
    //   -> 데이터 잇는지 없는지 확실하지 않으면 optional 쓰자!







    //page 는  카운트쿼리까지 가져오므로 간단한건 괜찮은데 join이 많으면 카운트쿼리 또한 join을 다해서 가져온다 = 성능저하
    // 사실 카운트쿼리는  join이 필요없다 어자피 옆에 붙는 테이블(조인이)없어도 개수는 같기때문에  join이 많으면 카운트쿼리는 때야 성능향샹
    // where 조건문이 따로 없어야한다
    @Query(value="select m from Member m left join m.team",
            countQuery = "select count(m) from Member m")
    //페이징처리 //pageable (인터페이스) 에  페이징 조건 넣기
    Page<Member> findByAge(int age, Pageable pageable);





    //벌크 연산은  db에 바로 값 넣는다.
    // 영속성 컨텍스트 무시한다
    //  영속성컨텍스트는 값 그대로... 그래서 벌크연산후 영속성컨텍스트 날려버려야함

    @Modifying(clearAutomatically = true)  //쿼리나가고 영속성 자동으로 비어주기
    @Query("update Member m set m.age=m.age+1 where m.age>=:age")
    int bulkAgePlus(@Param("age")int age);

    List<Member> findByUsername(String member3);






    // fetch Join  // 맴버조회할떄 그냥 다같이 한방쿼리로 가져온다    //  getTeam 할떄 프록시 가짜객체 안나온다
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = "team")  //fetch join jpql 귀찮을때 // 테이블 한방쿼리로 다가져오기
    List<Member> findAll();


    // jpql 에  + entityGraph 도 가능!
    @Query("select m from Member m ")
    @EntityGraph(attributePaths = "team")
    List<Member> findMemberFetchJoinEntityGraph();







    //스냅샷이랑 안만든다! 변경이 안된다는 보장 가정 //// 변경감지 체크 x  //성능최적화 가 미미하다.. // 얻는 이점이 있어야 넣는다..
    //처음에 튜닝(성능) 하면서 하는거 안좋다.
    @QueryHints(value=@QueryHint(name= "org.hiberante.readOnly", value="true"))
    Member findReadOnlyByUserName(String username);

    //db에 손못대게 하기
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUserName(String username);
}
