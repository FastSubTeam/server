package com.popple.server.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class RegisterToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String registerToken;

    private LocalDateTime createdAt;

    public static RegisterToken of(String email) {
        String token = generateToken();

        return RegisterToken.builder()
                .registerToken(token)
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static String generateToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
