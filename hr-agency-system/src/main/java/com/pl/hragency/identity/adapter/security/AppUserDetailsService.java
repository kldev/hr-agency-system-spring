package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.OrganizationContext;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);
    private final UserRepository userRepository;
    private final OrganizationContext organizationContext;

    public AppUserDetailsService(UserRepository userRepository, OrganizationContext organizationContext) {
        this.userRepository = userRepository;
        this.organizationContext = organizationContext;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername -> {}", username);
        User user = userRepository.findByEmailAndOrganizationId(username,
                organizationContext.getRequiredOrganizationId()).orElseThrow(() ->
                new UsernameNotFoundException(
                        "User not found"));;

        return new SecurityUser(
                user.id().value(),
                user.email(),
                user.organizationId().value(),
                List.of(user.role()),
                user.firstName() + " " + user.lastName()
        );
    }
}

