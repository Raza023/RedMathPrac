package com.practice.session2.User;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    public UserService(UserRepository repo)
    {
        this.userRepository = repo;
    }

    public User findByUserName(String userName)
    {
        return userRepository.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = findByUserName(username);
        if(user == null)
        {
            throw new UsernameNotFoundException("Invalid user: "+ username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(), true,
                true, true,true, AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));
    }
}
