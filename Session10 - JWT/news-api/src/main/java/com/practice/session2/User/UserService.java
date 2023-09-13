package com.practice.session2.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemp;

    public UserService(UserRepository repo)
    {
        this.userRepository = repo;
    }

//    public User findByUserName(String userName)
//    {
//        return userRepository.findByUserName(userName);
//    }

    @Cacheable("userNameCache") // Define a cache named "userCache"
    public User findByUserName(String userName)
    {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        return jdbcTemp.queryForObject(sql, new Object[]{userName}, new BeanPropertyRowMapper<>(User.class));
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
