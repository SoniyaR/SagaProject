package com.supermarket.OrderService.command.api.saga;

import com.supermarket.CommonService.commands.CompleteOrderCommand;
import com.supermarket.CommonService.commands.ShipOrderCommand;
import com.supermarket.CommonService.commands.ValidatePaymentCommand;
import com.supermarket.CommonService.events.OrderCompletedEvent;
import com.supermarket.CommonService.events.OrderShippedEvent;
import com.supermarket.CommonService.events.PaymentProcessedEvent;
import com.supermarket.CommonService.model.Users;
import com.supermarket.CommonService.queries.GetUserPaymentDetailsQuery;
import com.supermarket.OrderService.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("Order info created in saga for order id {}", event.getOrderId());

        //get details based on user id
        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery(event.getUserId());

        Users user = null;
        try {
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(Users.class))
                    .join();
        }catch (Exception e) {
            log.error(e.getMessage());
            //start compensating transaction
        }
        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .cardDetail(user.getCardDetail())
                .orderId(event.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .build();
        commandGateway.sendAndWait(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("Payment completed in saga for order id {}", event.getOrderId());
        try {
            ShipOrderCommand shipOrderCommand = ShipOrderCommand.builder()
                    .orderId(event.getOrderId())
                    .shipmentId(UUID.randomUUID().toString()).build();
            commandGateway.sendAndWait(shipOrderCommand);
        }catch(Exception e) {
            //start compensating transaction
            log.error(e.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderShippedEvent event) {
        log.info("Order Shipment completed in saga for order id {}", event.getOrderId());
        try {
            CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                    .orderStatus("APPROVED")
                    .orderId(event.getOrderId()).build();
            commandGateway.sendAndWait(completeOrderCommand);
        }catch(Exception e) {
            //start compensating transaction
            log.error(e.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    private void handle(OrderCompletedEvent event) {
        log.info("Order completed event in saga for order id {}", event.getOrderId());
        //notification service(optional)
    }
}
