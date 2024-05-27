package com.didacto.repository.pament;

import com.didacto.dto.pay.PayRequest;
import com.didacto.service.payment.PaymentCallbackRequest;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

public interface PaymentCustomRepository {
    PayRequest findRequestDto(String orderUid);
    // 결제(콜백)
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request);
}
