package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.User;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(username);
        if (findUser == null) {
            throw new RuntimeException();
        }
        return new UserAdapter(findUser);
    }
}
