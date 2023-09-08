package com.practice.session2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    //1) first method
    //to allow only one end point ("/api/v1/news")
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http.formLogin(Customizer.withDefaults());
//        http.authorizeHttpRequests(config -> config.requestMatchers(new AntPathRequestMatcher("/api/v1/news", "GET")).permitAll().anyRequest().authenticated());
//        return http.build();
//    }

    //2) second method  (to completely bypass the security)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/api/v1/news", "GET"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/**", "GET"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/news", "POST"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/**", "POST"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/news", "PUT"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/**", "PUT"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/search", "GET"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/search/**", "GET"))
                .requestMatchers(new AntPathRequestMatcher("/actuator", "GET"))
                .requestMatchers(new AntPathRequestMatcher("/actuator/**", "GET"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.formLogin(Customizer.withDefaults());
//        http.authorizeHttpRequests(config -> config.anyRequest().authenticated());

        http
                .headers()
                .referrerPolicy()
                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                .and();
//                .csrf().disable(); // Disable CSRF for simplicity

//        http.csrf(config -> config.disable());   //we don't have to disable it.
//        http.headers().referrerPolicy()
//                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.valueOf("strict-origin-when-cross-origin"));
        http.csrf(config -> config.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));   //now it will handle the csrf token request without any problem.
        http.authorizeHttpRequests(config -> config
                .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/**")).hasAnyAuthority("ACTUATOR")
                .anyRequest().authenticated());

        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
