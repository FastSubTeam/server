package com.popple.server.domain.user.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Role {
    USER(new ArrayList<>(List.of("USER", "user", "User"))),
    SELLER(new ArrayList<>(List.of("SELLER", "seller", "Seller")));

    private final ArrayList<String> symbols;

    Role(ArrayList<String> symbols) {
        this.symbols = symbols;
    }

    public static Role of(String roleSymbol) {
        return Arrays.stream(Role.values())
                .filter(role -> role.symbols.contains(roleSymbol))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }


    @Override
    public String toString() {
        return name();
    }
}
