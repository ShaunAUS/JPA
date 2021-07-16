package com.example.demo.entity;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


//테이블 만들기
@Entity
@Table(name="tbl_memo")
@Data
@Builder
@AllArgsConstructor   //builder, allargs.... noargs... 셋이 같이다닌다
@NoArgsConstructor
public class Memo {
    //모든 엔티티는 pk가 필요하다
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto increment
    private Long mno;

    private String memoText;


}
