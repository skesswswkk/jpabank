package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** *
 * xToOne관계 최적화
 * Order
 * Order -> Member : ManyToOne
 * Order -> Delivery : OneToOne
 *
 */
@RestController
@RequiredArgsConstructor //+final
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){ //문제점 : Entity(Order) 외부로 노출
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleDto> ordersV2(){ //v1해결책 : 엔티티를 DTO로 반환
        //문제점 : N + 1 (성능 문제)
        //ORDER 2개
        //N + 1 -> 1 + 회원N + 배송N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleDto> result = orders.stream()
                .map(o -> new OrderSimpleDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleDto> ordersV3(){ //v2해결책 : 페치 조인으로 성능 최적화
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderSimpleDto> result = orders.stream()
                .map(o -> new OrderSimpleDto(o))
                .collect(Collectors.toList());

        return result; //문제점 : select 시, order, member, delivery 다 조회
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){ //v3해결책 : JPA에서 DTO로 바로 조회
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class OrderSimpleDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleDto(Order o) {
            this.orderId = o.getId();
            this.name = o.getMember().getName();//Lazy 초기화로 Member table 터치
            this.orderDate = o.getOrderDate();
            this.orderStatus = o.getStatus();
            this.address = o.getDelivery().getAddress();//Delivery table 터치
        }
    }
}
