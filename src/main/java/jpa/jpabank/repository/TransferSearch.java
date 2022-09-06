package jpa.jpabank.repository;

import jpa.jpabank.domain.TransferStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferSearch {

    private String friendName;
    private TransferStatus transferStatus;
}