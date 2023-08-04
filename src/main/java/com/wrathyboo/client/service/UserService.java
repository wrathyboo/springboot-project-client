package com.wrathyboo.client.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wrathyboo.client.entities.Token;
import com.wrathyboo.client.entities.User;
import com.wrathyboo.client.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final TokenRepository tokenRepository;
	
    
	public String getToken() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Integer userId;
		if (principal instanceof User) {
			userId = ((User)principal).getId();
		} else {
			userId = null;
		}
      
	  List<Token> validTokens = tokenRepository.findAllValidTokenByUser(userId);
	  String getFirstValidToken = "";
	 for (Token x : validTokens) {
		 getFirstValidToken = x.getToken();
	 }
	final String access_token = getFirstValidToken;
	return access_token;
	}
   
}
