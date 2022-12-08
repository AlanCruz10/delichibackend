package com.delichi.delichibackend.security;

import com.delichi.delichibackend.security.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        final String[] ROTES_NON_PROTECTED = {"/restaurant/restaurants",
                                                    "/restaurant/{id}",
                                                    "/restaurant/restaurants/name/{name}",
                                                    "/ceo",
                                                    "/ceo/login",
                                                    "/image/ListImages/{idRestaurant}",
                                                    "/image/getBanner/{idRestaurant}",
                                                    "/image/getLogo/{idRestaurant}",
                                                    "/comment/restaurant/{restaurantId}/comments",
                                                    "/zone/{id}",
                                                    "/zone/zones",
                                                    "/zone/{id}/restaurants",
                                                    "/user",
                                                    "/user/login"};

        return http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(ROTES_NON_PROTECTED)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();


    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
