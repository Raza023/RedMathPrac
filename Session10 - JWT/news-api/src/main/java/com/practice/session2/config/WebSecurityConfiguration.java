package com.practice.session2.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMessage;
//import org.springframework.http.HttpMethod;
////import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
//import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableMethodSecurity
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer()
//    {
//        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/api/v1/news", "GET"))
//                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/**", "GET"))
//                .requestMatchers(new AntPathRequestMatcher("/api/v1/news", "POST"))
//                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/**", "POST"))
//                .requestMatchers(new AntPathRequestMatcher("/api/v1/news", "PUT"))
//                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/**", "PUT"))
//                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/search", "GET"))
//                .requestMatchers(new AntPathRequestMatcher("/api/v1/news/search/**", "GET"))
//                .requestMatchers(new AntPathRequestMatcher("/actuator", "GET"))
//                .requestMatchers(new AntPathRequestMatcher("/actuator/**", "GET"));
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http.formLogin(Customizer.withDefaults());
//
//
//        http.csrf(config -> config.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));   //now it will handle the csrf token request without any problem.
//        http.authorizeHttpRequests(config -> config
//                .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/**")).hasAnyAuthority("ACTUATOR")
//                .anyRequest().authenticated());
//
//        return http.build();
//    }
//}


import com.practice.session2.User.UserService;
import com.practice.session2.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests().antMatchers("/api/v1/news/authenticate").permitAll() // Allow public access
                .antMatchers("/actuator/**").hasAnyAuthority("ACTUATOR") // Secure actuator endpoints
                .anyRequest().authenticated() // Secure all other endpoints
                .and().exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
