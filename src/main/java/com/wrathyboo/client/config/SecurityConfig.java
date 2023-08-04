package com.wrathyboo.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;


import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private static final String[] AUTH_WHITELIST = {
	        "/",
	        "/home",
	        "/blog",
	        "/contact",
	        "/about",
	        "/faq",
	        "/category",
	        "/products",
	        "/product",
	        "/product/{id}",
	        "/error",
	        "/register",
	        "/authenticate",
	        "/process_login",
	        "/assets/**"
	};
	private static final String[] AUTH_USER = {
	        "/cart",
	        "/checkout",
	        "/account",
	        "/wishlist",
	        "/add-to-cart/{id}"
	};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> auth
		           .requestMatchers(AUTH_WHITELIST).permitAll()
                   .anyRequest().authenticated())
				   .csrf(csrf -> csrf.disable())
			       .formLogin(form -> form
			    		   .usernameParameter("username")
		                   .passwordParameter("password")
			    		   .loginPage("/login")
			    		   .loginProcessingUrl("/process_login")
			    		   .failureUrl("/login?error")
			    		   .defaultSuccessUrl("/account",true)
			    		   .permitAll())
			       .logout(logout -> logout 
			    		   .logoutUrl("/logout")
			    		   .logoutSuccessUrl("/login"))
			       .build();
	}
}