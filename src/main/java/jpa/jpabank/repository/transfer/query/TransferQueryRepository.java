package jpa.jpabank.repository.transfer.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TransferQueryRepository {

    private final EntityManager em;

    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 N 번 -> N + 1 문제점
     * 단건 조회에서 많이 사용하는 방식
     */
    //v4
    public List<TransferQueryDto> findTransferQueryDtos() { //루트 조회(toOne 코드를 모두 한번에 조회)
        List<TransferQueryDto> result = findTransfers(); //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)

        result.forEach(o -> {
            List<TransferAccountQueryDto> transferAccounts = findTransferItems(o.getTransferId());
            o.setTransferAccounts(transferAccounts);
        });
        return result;
    }

    /**
     * 1:N 관계(컬렉션)를 제외한 나머지를 한번에 조회
     */
    private List<TransferQueryDto> findTransfers() {
        return em.createQuery(
        "select new jpa.jpabank.repository.transfer.query.TransferQueryDto(t.id, f.name, t.transferDate, t.transferStatus)" +
                " from Transfer t" +
                " join t.friend f", TransferQueryDto.class)
            .getResultList();
    }

    /**
     * 1:N 관계인 transferAccounts 조회
     */
    private List<TransferAccountQueryDto> findTransferItems(Long transferId) {
        return em.createQuery(
        "select new jpa.jpabank.repository.transfer.query.TransferAccountQueryDto(oa.transfer.id, a.name, oa.count)" +
                " from TransferAccount ta" +
                " join ta.account a" +
                " where ta.transfer.id = : transferId", TransferAccountQueryDto.class)
        .setParameter("transferId", transferId)
        .getResultList();
    }

    //v5
    public List<TransferQueryDto> findAllByDto_optimization() {
        //루트 조회(toOne 코드를 모두 한번에 조회)
        List<TransferQueryDto> result = findTransfers();//TransferQueryDto 2개

        List<Long> transferIds = result.stream()
                .map(o -> o.getTransferId())
                .collect(Collectors.toList());

        //transferItem 컬렉션을 MAP 한방에 조회
        Map<Long, List<TransferAccountQueryDto>> transferAccountMap = findTransferAccountMap(transferIds);

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행X)
        result.forEach(o -> o.setTransferAccounts(transferAccountMap.get(o.getTransferId())));

        return result;
    }

    private Map<Long, List<TransferAccountQueryDto>> findTransferAccountMap(List<Long> transferIds) {

        List<TransferAccountQueryDto> transferItems = em.createQuery(
            "select new jpa.jpabank.repository.transfer.query.TransferAccountQueryDto(ta.transfer.id, a.name, ta.transferMoney)" +
                    " from TransferAccount ta" +
                    " join ta.account a" +
                    " where ta.transfer.id in :transferIds", TransferAccountQueryDto.class)
            .setParameter("transferIds", transferIds)
            .getResultList();

        return transferItems.stream().collect(Collectors.groupingBy(TransferAccountQueryDto::getTransferId));
    }

    //v6
    public List<TransferFlatDto> findAllByDto_flat() {

        return em.createQuery(
            "select new jpa.jpabank.repository.transfer.query.TransferFlatDto(t.id, f.name, t.transferDate, t.status, a.name, ta.count)" +
                    " from Transfer t" +
                    " join t.friend f" +
                    " join t.transferAccounts ta" +
                    " join ta.account a", TransferFlatDto.class)
            .getResultList();
    }
}