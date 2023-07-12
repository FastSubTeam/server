package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.user.repository.UserRepository;
import com.popple.server.domain.user.vo.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member findMember = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        return new UserAdapter(findMember);
    }
}
