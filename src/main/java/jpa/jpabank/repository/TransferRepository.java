package jpa.jpabank.repository;

import jpa.jpabank.domain.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransferRepository {

    private final EntityManager em;

    public void save(Transfer transfer){
        em.persist(transfer);
    }

    public Transfer findOne(Long id){
        return em.find(Transfer.class, id);
    }

    public List<Transfer> findAll(){
        return em.createQuery("select t from Transfer t", Transfer.class)
                .getResultList();
    }

    //simpleV1, simpleV2, V1, v2
    public List<Transfer> findAllByString(TransferSearch transferSearch){

        String jpql = "select t From Transfer t join t.friend f";
        boolean isFirstCondition = true;

        //이체 상태 검색
        if (transferSearch.getTransferStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " t.transferStatus = :transferStatus";
        }

        //친구 이름 검색
        if (StringUtils.hasText(transferSearch.getFriendName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Transfer> query = em.createQuery(jpql, Transfer.class) .setMaxResults(1000); //최대 1000건
        if (transferSearch.getTransferStatus() != null) {
            query = query.setParameter("transferStatus", transferSearch.getTransferStatus());
        }
        if (StringUtils.hasText(transferSearch.getFriendName())) {
            query = query.setParameter("name", transferSearch.getFriendName());
        }
        return query.getResultList();
    }

    //simpleV3
    public List<Transfer> findAllWithFriend() {
        return em.createQuery(
        "select t from Transfer t" +
                " join fetch t.friend f" , Transfer.class)
                    .getResultList();
    }

    //v3
    public List<Transfer> findAllWithAccount() {
        return em.createQuery(
                "select t from transfer t" +
                    " join fetch t.friend f" +
                    " join fetch t.transferAccounts ta" +
                    " join fetch ta.account a", Transfer.class)
                .getResultList();
    }

    //v3.1
    public List<Transfer> findAllWithFriend(int offset, int limit) {
        return em.createQuery(
        "select t from transfer t" +
                " join fetch t.friend f" , Transfer.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }
}