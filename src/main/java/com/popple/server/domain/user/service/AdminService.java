package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.Admin;
import com.popple.server.domain.user.dto.LoginRequestDto;
import com.popple.server.domain.user.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdminRepository adminRepository;

    public boolean checkAdmin(LoginRequestDto loginRequestDto) {
        Admin admin = adminRepository.findByEmail(loginRequestDto.getEmail());

        if (admin == null) {
            return false;
        }

        if (bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), admin.getPassword())) {
            return true;
        }

        return false;
    }

    public Admin getAdmin(String email) {
        return adminRepository.findByEmail(email);
    }
}
