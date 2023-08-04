package com.wrathyboo.client.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.wrathyboo.client.entities.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Controller
@RequiredArgsConstructor
public class AuthenticationController{


	@PostMapping("/register")
	public String register(@ModelAttribute("registerRes") RegisterRequest request){
		WebClient client = WebClient.create();
		val response = client.post()
			    .uri( "http://localhost:8080/api/v1/auth/register" )
			    .accept(MediaType.APPLICATION_JSON)
//			    .headers(h -> h.setBearerAuth(userService.getToken()))
//			    .body(BodyInserters.fromValue(user))
//			    .exchange()
//			    .block();
			    .bodyValue(request)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		return "redirect:login";
	}
	
}

