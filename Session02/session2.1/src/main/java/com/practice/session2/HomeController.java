package com.practice.session2;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/news")
public class HomeController {

    @GetMapping
    public ResponseEntity<Map<String, String>> simpleMapingFunctionThatReturnsText(){
        return ResponseEntity.ok(Map.of("message","Current time is: "+ LocalDateTime.now()));
    }

}
