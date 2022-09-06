package jpa.jpabank.repository;

import jpa.jpabank.domain.Friend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendRepository {

    private final EntityManager em;

    public void save(Friend friend){
        em.persist(friend);
    }

    public Friend findOne(Long id){
        return em.find(Friend.class, id);
    }

    public List<Friend> findAll(){
        return em.createQuery("select f from Friend f", Friend.class)
                   .getResultList();
    }

    public List<Friend> findByName(String name){
        return em.createQuery("select f from Friend f where f.name = :name", Friend.class)
                .setParameter("name", name)
                .getResultList();
    }
}
