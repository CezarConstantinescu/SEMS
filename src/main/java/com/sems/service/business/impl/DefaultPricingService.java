package com.sems.service.business.impl;

import com.sems.model.Concert;
import com.sems.model.Event;
import com.sems.model.Wedding;
import com.sems.model.User;
import com.sems.service.business.PricingService;

import java.math.BigDecimal;

public class DefaultPricingService implements PricingService {

    @Override
    public BigDecimal calculatePrice(Event event, User user) {
        if (event == null) return BigDecimal.ZERO;

        // simple heuristics based on event type
        if (event instanceof Concert) {
            return new BigDecimal("120.00");
        } else if (event instanceof Wedding) {
            return new BigDecimal("0.00");
        }
        return new BigDecimal("50.00");
    }
}
