package com.popple.server.domain.user.service;

import com.popple.server.domain.entity.Member;
import com.popple.server.domain.entity.Seller;
import com.popple.server.domain.user.exception.UserErrorCode;
import com.popple.server.domain.user.exception.UserUnauthorizedException;
import com.popple.server.domain.user.repository.MemberRepository;
import com.popple.server.domain.user.repository.SellerRepository;
import com.popple.server.domain.user.vo.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member findMember = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UserUnauthorizedException(UserErrorCode.NOT_FOUND));
        return new UserAdapter(findMember);
    }

    public UserDetails loadSellerByUsername(String id) {
        Seller seller = sellerRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new UserUnauthorizedException(UserErrorCode.NOT_FOUND));

        return new UserAdapter(seller);
    }
}
