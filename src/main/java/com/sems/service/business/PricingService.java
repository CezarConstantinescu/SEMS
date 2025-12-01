package com.sems.service.business;

import com.sems.model.Event;
import com.sems.model.User;

import java.math.BigDecimal;

public interface PricingService {

    BigDecimal calculatePrice(Event event, User user);

}
