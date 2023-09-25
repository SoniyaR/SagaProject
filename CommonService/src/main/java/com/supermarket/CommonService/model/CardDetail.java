package com.supermarket.CommonService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardDetail {
    private String name;
    private String cardNumber;
    private Integer validUntilMonth;
    private Integer validUntilYear;
    private Integer cvv;
}
