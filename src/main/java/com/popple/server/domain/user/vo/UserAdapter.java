package com.popple.server.domain.user.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class UserAdapter extends User {
    public UserAdapter(com.popple.server.domain.entity.User user) {
        super(user.getId().toString(), user.getPassword(), List.of(new SimpleGrantedAuthority(Role.USER.name())));
    }
}
