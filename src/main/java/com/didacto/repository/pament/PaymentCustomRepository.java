package com.didacto.repository.pament;

import com.didacto.domain.Payment;
import com.siot.IamportRestClient.response.IamportResponse;

public interface PaymentCustomRepository {
    RequestPayDto findRequestDto(String orderUid);
    // 결제(콜백)
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request);
}
