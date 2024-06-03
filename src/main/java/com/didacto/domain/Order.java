package com.didacto.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders") // Mariadb에서 order가 예약어로 되어있기 때문에 @Table은 필수로 작성
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    private Grade itemName; // 프리미엄, 프리티어
    private String orderUid; //주문번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public Order(Long price, Grade itemName, String orderUid, Member member, Payment payment) {
        this.price = price;
        this.itemName = itemName;
        this.orderUid = orderUid;
        this.member = member;
        this.payment = payment;
    }
}
