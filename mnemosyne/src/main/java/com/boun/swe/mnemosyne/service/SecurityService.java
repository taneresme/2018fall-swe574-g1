package com.boun.swe.mnemosyne.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public SecurityService(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public boolean authenticate(String username, String password) {
        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        if (userDetails == null) {
            return false;
        }

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        /* If there is any exceptions below, we will be handling the exception
         * not to allow Spring to redirect to its error pages. */
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Throwable e) {
            return false;
        }

        if (authenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("User: {} successfully authenticated!", username);
        }
        return true;
    }

    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }

        return null;
    }
}
