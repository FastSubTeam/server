package com.popple.server.domain.user.vo;

import com.popple.server.domain.entity.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserAdapter extends User {
    public UserAdapter(Member member) {
        super(member.getId().toString(), member.getPassword(), List.of(new SimpleGrantedAuthority(Role.USER.name())));
    }
}
