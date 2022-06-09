package hello.servlet.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private Long id;
    private String userName;
    private int age;

    public Member(String hello, int i) {
    }

    public Member(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
