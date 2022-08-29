package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //Merge
    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    //변경 감지
    @Transactional
    /*컨트롤러에서 어설프게 엔티티(book)를 생성하지 마세요.
    public Item updateItem(Long itemId, Book param){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        return findItem;
    }
    */
    public Item updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        return findItem;
    }

//    public Item findOne(Long id)
    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }

//    public List<Item> findAll()
    public List<Item> findItems(){
        return itemRepository.findAll();
    }

}
