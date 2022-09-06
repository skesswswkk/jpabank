package jpa.jpabank.domain;

import jpa.jpabank.domain.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dType")
@Getter @Setter
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    private String name;
    private int stockDeposit;

    //==비즈니스 로직==//
    //(도메인 주도 설계)
    /**
     * stockDeposit 증가
     */
    public void addStock(int quantity){
        this.stockDeposit += quantity;
    }

    /**
     * stockDeposit 감소
     */
    public void removeStock(int quantity){
        int restStock = (this.stockDeposit -= quantity);
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockDeposit = restStock;
    }
}

