package com.sheryians.major.model;

import java.util.List;

public interface UserBuilder {
    UserBuilder firstName(String firstName);
    UserBuilder lastName(String lastName);
    UserBuilder email(String email);
    UserBuilder password(String password);
    UserBuilder roles(List<Role> roles);
    User build();
}
