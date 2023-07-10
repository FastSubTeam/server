package com.popple.server.common.converter;

import com.popple.server.domain.user.vo.Role;
import org.springframework.core.convert.converter.Converter;

public class RoleRequestConverter implements Converter<String, Role> {

    @Override
    public Role convert(String roleSymbol) {
        return Role.of(roleSymbol);
    }
}
