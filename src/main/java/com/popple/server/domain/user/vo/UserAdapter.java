package com.popple.server.domain.user.vo;

import com.popple.server.domain.entity.Admin;
import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Seller;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserAdapter extends User {
    public UserAdapter(Member member) {
        super(member.getId().toString(), member.getPassword(), List.of(new SimpleGrantedAuthority(Role.ROLE_USER.name())));
    }

    public UserAdapter(Seller seller) {
        super(seller.getId().toString(), seller.getPassword(), List.of(new SimpleGrantedAuthority(Role.ROLE_SELLER.name())));
    }

    public UserAdapter(Admin admin) {
        super(admin.getId().toString(), admin.getPassword(), List.of(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name())));
    }
}
