package com.supermarket.OrderService.command.api.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    private String productId;
    private String userId;
    private String address;
    private Integer quantity;
    private String orderStatus;
}
