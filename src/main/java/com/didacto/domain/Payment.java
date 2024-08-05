package com.didacto.domain;

import com.didacto.common.BaseEntity;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String paymentUid;

    @Builder
    public Payment(Long price, PaymentStatus status){
        this.price = price;
        this.status = status;
    }
    public void changePaymentBySuccess(PaymentStatus status, String paymentUid) {
        this.status = status;
        this.paymentUid = paymentUid;
    }
}
