package study.datajpa.dto;

public class MemberDTO {

    private Long id;
    private String usernamel;
    private String teamName;

    public MemberDTO(Long id, String username, String teamName) {
        this.id = id;
        this.usernamel = username;
        this.teamName = teamName;
    }

}
