package com.example.demo.repository;

import com.example.demo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
                                                    //엔티티, pk값
public interface MemoRepository extends JpaRepository<Memo, Long> {
}
