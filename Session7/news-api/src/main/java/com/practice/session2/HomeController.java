package com.practice.session2;


import com.practice.session2.User.User;
import com.practice.session2.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/helloworld")
public class HomeController {

    private final UserService userService;
    @Autowired
    public HomeController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> simpleMapingFunctionThatReturnsText(){
        User user = userService.findByUserName("reporter");
        String role = user.getRoles();
        // Retrieve the current Authentication object
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String AuthUserName = "";
        if (auth != null) {
            AuthUserName =  auth.getName();
        } else {
            // Handle the case when there is no authentication
            AuthUserName =  "Anonymous";
        }
        return ResponseEntity.ok(Map.of("message","Current time is: "+ LocalDateTime.now() +
                ". Role of my user is: " + role + ". Authenticated user name is: "+ AuthUserName));
    }

}
