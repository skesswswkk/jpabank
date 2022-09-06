package jpa.jpabank.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountForm {

    private Long id;
    private String name;
    private int stockDeposit;
}
