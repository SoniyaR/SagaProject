package com.supermarket.OrderService.command.api.events;

import com.supermarket.CommonService.events.OrderCompletedEvent;
import com.supermarket.OrderService.command.api.data.Order;
import com.supermarket.OrderService.command.api.data.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderEventsHandler {

    @Autowired private OrderRepository orderRepository;

    @EventHandler
    public void on(OrderCreatedEvent event) {
        Order order = new Order();
        BeanUtils.copyProperties(event, order);
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent event) {
        Optional<Order> orderOpt = orderRepository.findById(event.getOrderId());
        orderOpt.ifPresent(order -> {
            order.setOrderStatus(event.getOrderStatus());
            orderRepository.save(order);
        });
    }
}
