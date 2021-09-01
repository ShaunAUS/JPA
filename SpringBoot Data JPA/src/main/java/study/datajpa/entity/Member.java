package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter   //엔티티에서는 잘안쓴다
@ToString(of = {"id","username","age"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //JPA는 매개변수 없는 생성자 필요로한다 // protected// private xxx
public class Member {


    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;
    private String username;
    private int age;


    @ManyToOne(fetch = FetchType.LAZY)  //eager는 성능최적화 힘듬
    @JoinColumn(name="team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {

        this.username=username;
        this.age=age;

        //fk
        if(team !=null) {
            //양방향이니 양쪽 다 영항주기 = 편의메서드
            changeTeam(team);
        }



    }


    // 양방향 양쪽 다 바꾸기 = 편의메서드  // =setTeam
    public void changeTeam(Team team){

        this.team=team;
        team.getMembers().add(this);
    }



}
