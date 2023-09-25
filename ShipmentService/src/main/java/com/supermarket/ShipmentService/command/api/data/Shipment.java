package com.supermarket.ShipmentService.command.api.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "shipment")
public class Shipment {
    @Id
    private String shipmentId;
    private String orderId;
    private String paymentStatus;
}
