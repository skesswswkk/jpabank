package jpa.jpabank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Friend {

    @Id @GeneratedValue
    @Column(name = "friend_id")
    private Long id;

    @NotEmpty
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "friend")
    private List<Transfer> transfers = new ArrayList<>();
}
