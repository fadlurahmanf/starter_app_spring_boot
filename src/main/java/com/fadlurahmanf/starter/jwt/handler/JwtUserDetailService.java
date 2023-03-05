package com.fadlurahmanf.starter.jwt.handler;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.identity.dto.entity.IdentityEntity;
import com.fadlurahmanf.starter.identity.handler.repository.IdentityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtUserDetailService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(JwtUserDetailService.class);
    @Autowired
    IdentityRepository identityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<IdentityEntity> optIdentity = identityRepository.findByEmail(username);
        if(optIdentity.isEmpty()){
            throw new UsernameNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        IdentityEntity identity = optIdentity.get();


        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("user"));
        return new org.springframework.security.core.userdetails.User(
                identity.email,
                identity.password,
                grantedAuthorities
        );
    }
}
