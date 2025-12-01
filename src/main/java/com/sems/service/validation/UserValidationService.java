package com.sems.service.validation;

import com.sems.model.User;

public interface UserValidationService {

    void validateEmail(User user) throws IllegalArgumentException;

    void validateAccountStatus(User user) throws IllegalStateException;
}
