package com.wrathyboo.client.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wrathyboo.client.entities.RegisterRequest;
import com.wrathyboo.client.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BlogController {
	private final ProductService productService;
	
    @RequestMapping("blog")
    public String blogPage(Model model, Authentication auth){
    	productService.getUserCart(auth, model);
        model.addAttribute("bcrumbTitle","Blogs");
        model.addAttribute("bcrumbSubtitle","News");
        model.addAttribute("registerRes", new RegisterRequest());
        return "blog";
    }
}
