package jpa.jpabank.repository.transfer.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransferSimpleQueryRepository {

    private final EntityManager em;

    public List<TransferSimpleQueryDto> findTransferDtos() {
        return em.createQuery(
                        "select new jpa.jpabank.repository.transfer.simplequery.TransferSimpleQueryDto(t.id, f.name, t.transferDate, t.status)" +
                                " from Transfer t" +
                                " join t.friend f", TransferSimpleQueryDto.class)
                .getResultList();
    }
}
