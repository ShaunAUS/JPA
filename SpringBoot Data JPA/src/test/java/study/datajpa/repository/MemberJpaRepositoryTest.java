package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional // org  기능이 더 좋다 //jpa 모든 데이터 변경은 트랜스액션 안에서 해야한다 // 테스트 끝날때 다 롤백시킨다
//같은 transactional 안에서는 같은 영속성 보장 // 저장한애나 찾아온애나 같은 인스턴스 =1차캐쉬
@Rollback(false)  //롤백안하고 커밋하기(false)// 영속성 컨텍스트 플러쉬되서 db에 반영된다  롤백 안하니까
    //JPA  = 변경,수정 = Transactional , Rollback = 변경감지를 통한 커밋으로 수행된다
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository repository;

    @Test
    public void test(){
        Member member= new Member("memberA");
        Member saveMember=repository.save(member);

        Member findMember=repository.find(saveMember.getId());


        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

}