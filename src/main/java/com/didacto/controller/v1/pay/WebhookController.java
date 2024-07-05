package com.didacto.controller.v1.pay;


import com.didacto.dto.pay.WebhookPayloadRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    @PostMapping("/webhook")
    public String handleWebhook(@RequestBody WebhookPayloadRequest payload) {
        String impUid = payload.getImpUid();
        String merchantUid = payload.getMerchantUid();
        String status = payload.getStatus();
        int amount = payload.getAmount();

        // 결제 상태 업데이트 로직
        if ("paid".equals(status)) {
            // 결제 성공 처리
            System.out.println("Payment " + merchantUid + " is paid with amount " + amount);
        } else {
            // 결제 실패 처리
            System.out.println("Payment " + merchantUid + " failed.");
        }

        return "Webhook received";
    }
}
