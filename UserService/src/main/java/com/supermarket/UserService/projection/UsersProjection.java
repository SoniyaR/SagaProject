package com.supermarket.UserService.projection;

import com.supermarket.CommonService.model.CardDetail;
import com.supermarket.CommonService.model.Users;
import com.supermarket.CommonService.queries.GetUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UsersProjection {

    @QueryHandler
    public Users getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
//        Ideally Get Details from DB
        CardDetail cardDetail = CardDetail.builder()
                .name("soniya")
                .cardNumber("123456789")
                .cvv(111)
                .validUntilMonth(12)
                .validUntilYear(28)
                .build();
        return Users.builder()
                .userId(query.getUserId())
                .firstName("soniya")
                .lastName("sachin")
                .cardDetail(cardDetail)
                .build();
    }
}
