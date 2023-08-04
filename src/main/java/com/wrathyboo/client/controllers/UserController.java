package com.wrathyboo.client.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

import com.wrathyboo.client.entities.RegisterRequest;
import com.wrathyboo.client.service.ProductService;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final ProductService productService;
	
    @RequestMapping("login")
    public String loginPage(Model model, Authentication auth){
    	model.addAttribute("registerRes", new RegisterRequest());
        return "login";
    }

    @RequestMapping("account")
    @PreAuthorize("hasRole('USER')")
    public String userAccount(Model model, Authentication auth){
    	productService.getUserCart(auth, model);
    	model.addAttribute("registerRes", new RegisterRequest());
        return "dashboard";
    }
    
}
