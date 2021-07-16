package com.example.demo.repository;

import com.example.demo.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

//엔티티, pk값
public interface MemoRepository extends JpaRepository<Memo, Long> {

    // JpaRepository 기능만들기// pageable 파라미터는 모든쿼리 메서드에 적용가능


        //쿼리메서드
        //목록
        List<Memo>findByMnoBetweenOrderByMnoDesc(Long from, Long to);
        //정렬   //
        Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
        //삭제
        void deleteMemoByMnoLessThan(Long num);
}
