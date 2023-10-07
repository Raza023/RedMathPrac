package com.github.workshop.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    public static final String STATUS_ACTIVE = "ACTIVE";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepository repository;

    @Value("${login.attempts.max:3}")
    private int loginAttemptsMax = 3;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findByUserName(String userName) {
        return repository.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.debug("user login start: {}", userName);
        User user = findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid user: " + userName);
        }
        boolean enabled = STATUS_ACTIVE.equals(user.getStatus());
        boolean notLocked = user.getLoginAttempts() == null || user.getLoginAttempts() < loginAttemptsMax;
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), enabled,
                true, true, notLocked, AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
    }
}