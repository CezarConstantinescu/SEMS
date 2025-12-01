package com.sems.service.validation.impl;

import com.sems.model.User;
import com.sems.service.validation.UserValidationService;

import java.util.regex.Pattern;

public class UserValidationServiceImpl implements UserValidationService {

    private static final Pattern EMAIL_RE = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    public void validateEmail(User user) throws IllegalArgumentException {
        if (user == null) throw new IllegalArgumentException("User is null");
        if (user.getEmail() == null || !EMAIL_RE.matcher(user.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email for user: " + user.getEmail());
        }
    }

    @Override
    public void validateAccountStatus(User user) throws IllegalStateException {
        // No account state in model — placeholder for real checks
    }
}
