package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.lang.model.element.Name;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //save
    public void save(Item item){
        if(item.getId() == null) em.persist(item);//신규등록
        else em.merge(item);//업데이트
    }

    //findOne
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    //findAll
    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
