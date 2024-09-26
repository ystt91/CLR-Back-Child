package org.zerock.mallapi.domain;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList")
/*
* 1. 무한 루프 방지

* @ToString 어노테이션은 클래스의 모든 필드를 문자열로 변환하는
* toString() 메서드를 자동으로 생성해줍니다.

* 만약 연관 관계를 맺고 있는 엔티티(예: MemberRole)가 다시 @ToString을 사용해
* Member를 참조하면, Member와 MemberRole 간의 상호 참조가 발생할 수 있습니다.

* 이로 인해 toString() 호출 시 계속해서 두 객체가 서로를 참조하면서 무한 루프에 빠지게 되고,
* 결국 StackOverflowError가 발생할 수 있습니다.


2. 성능 문제

* @ToString이 연관된 엔티티를 포함하게 되면, 해당 엔티티의 모든 데이터를 문자열로 변환해야 합니다.

* 연관된 엔티티의 데이터가 많거나 깊은 구조를 가지는 경우, toString() 메서드
* 호출 시 많은 양의 데이터를 처리해야 하기 때문에 성능 저하가 발생할 수 있습니다.

* 특히 FetchType.LAZY로 설정된 컬렉션은 toString()을 호출하는 시점에
* 데이터베이스에서 불필요하게 조회(fetch)될 수 있어 성능이 저하될 수 있습니다.
*
* */
public class Member {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

    public void clearRole(MemberRole memberRole) {
        memberRoleList.clear();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePw(String pw) {
        this.pw = pw;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }
}
