package com.supermarket.OrderService.command.api.aggregate;

import com.supermarket.CommonService.commands.CompleteOrderCommand;
import com.supermarket.CommonService.events.OrderCompletedEvent;
import com.supermarket.OrderService.command.api.command.CreateOrderCommand;
import com.supermarket.OrderService.command.api.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String address;
    private Integer quantity;
    private String orderStatus;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        //validate the command
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.address = event.getAddress();
        this.orderStatus = event.getOrderStatus();
        this.quantity = event.getQuantity();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        //validate the command
        //publish order completed event
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder().orderId(completeOrderCommand.getOrderId())
                .orderStatus(completeOrderCommand.getOrderStatus()).build();
        AggregateLifecycle.apply(orderCompletedEvent);
    }
    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
//        this.orderId = event.getOrderId();
        this.orderStatus = event.getOrderStatus();
    }
}
