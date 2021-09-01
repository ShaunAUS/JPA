package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@ToString(of ={"id","name"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;


    @OneToMany(mappedBy = "team")  //fk 가없는쪽// 양방향 맵핑이면 한쪽 에 mappedby적어야한다.
    private List<Member> members = new ArrayList<>();



    public Team(String name) {
        this.name=name;
    }
}

