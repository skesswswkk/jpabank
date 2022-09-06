package jpa.jpabank.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class FriendForm {

    @NotEmpty(message = "친구 이름은 필수입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}

