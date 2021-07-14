package com.example.demo.repository;

import com.example.demo.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
public class MemoRepositoryTests {

        @Autowired
    MemoRepository memoRepository;


        //insert문 save
        @Test
    public void testInsertDummies(){
            IntStream.rangeClosed(1,100).forEach((i->{
                Memo memo = Memo.builder().memoText("Sample..."+i).build();
                memoRepository.save(memo);
            }));
        }

        @Test
    public void testSelect(){
            //데이터베이스에 존재하는 mno
            Long mno=100L;

            //select 문 = findBy Id
            //Optional 로 반환
            //바로 실행된다
            Optional<Memo> result=memoRepository.findById(mno);

            System.out.println("====================");

            if(result.isPresent()){
                Memo memo=result.get();
            }
        }


    @Transactional //getOne는 선언해야함
    @Test
    public void testSelect2(){
        //데이터베이스에 존재하는 mno
        Long mno=100L;

        //select 문 = getOne()
        //필요할떄 실행된다
        Memo memo=memoRepository.getOne(mno);

        System.out.println("====================");

        System.out.println(memo);
    }

    //처음에 select 문이 먼저 실행되 이런 객체가 있는지 확인
    //있으면 update문 실행 /없으면 insert문실행
    @Test
    public void testUpdate(){
            Memo memo=Memo.builder().mno(100L).memoText("hi").build();

            System.out.println(memoRepository.save(memo));
    }

    @Test
    //select문으로 해당id 있는지 확인하고 있으면 삭제, 없으면 emptyResult exception 발생
    public void testDelete(){
            Long mno =100L;
            memoRepository.deleteById(mno);
    }
    //--------------------------------------페이징시작--------------------------------------

    @Test
    public void testPageDefault(){
            //findAll
            //pageable =페이지처리에 필요한 정보전달하는  용도 인터페이스
            //pageRequest 클래스로 구현해야함 // 생성자 protected
            //1페이지 10개씩 데이터가 있다
            Pageable pageable= PageRequest.of(0,10);
            
            //페이징처리, 정렬 =finAll()
            Page<Memo> result=memoRepository.findAll(pageable);
            //page 는 여러가지 메소드를 지원한다.
            //getTotalPages(), getTotalElements(),
            // getNumber()=현재페이지번호 getSize()=페이지당 데이터개수,hasNext()=다음페이지존재여부,isFirst()=시작페이지(0)여부
            //getContent() =전체출력  List<엔티티타입> 으로 반환
            System.out.println(result);
    }

    @Test
    public void testSort(){
            //sort + findAll
            //mno값 기준 역순
            Sort sort1=Sort.by("mno").descending();
            //pageRequest 파라미터로 sort 넣을수있다.
            Pageable pageable= PageRequest.of(0,10,sort1);
            Page<Memo> result=memoRepository.findAll(pageable);
            //결과출력
            result.get().forEach(memo->{
                System.out.println(memo);
            });

            //--- and() 로 조건 2개할수 있다.
            //Sort sort1=Sort.by("mno").descending();
            //Sort sort2=Sort.by("text").descending();
            //Sort sortAll=Sort1.and(sort2).descending();
            //Pageable pageable= PageRequest.of(0,10,sortAll);
    }
}
