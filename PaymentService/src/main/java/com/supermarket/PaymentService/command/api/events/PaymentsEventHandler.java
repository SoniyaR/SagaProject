package com.supermarket.PaymentService.command.api.events;

import com.supermarket.CommonService.events.PaymentProcessedEvent;
import com.supermarket.PaymentService.command.api.data.Payment;
import com.supermarket.PaymentService.command.api.data.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PaymentsEventHandler {

    @Autowired
    private PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .timeStamp(new Date()).build();
        paymentRepository.save(payment);
    }
}
