package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class  Member {

    @Id @GeneratedValue //DB가 시퀀스값 자동생성
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;
    /*
    상황 : "name" -> "username" : API스펙 자체가 "username"으로 변경
    문제점 : Entity 변경 -> API스펙 자체가 변경된다. Entity 는 여러 곳에서 사용하므로, 변경 가능성 높다.
    해결책 : API스펙을 위한 별도의 DTO를 사용. Entity : API스펙 = 1:1 매핑되도록 한다.
    */
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

//  @JsonIgnore : Entity에 presentation계층(화면에 뿌리기 위한)을 위한 로직 추가 -> 문제
    @Embedded
    private Address address;
}
