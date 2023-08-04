package com.wrathyboo.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wrathyboo.client.entities.RegisterRequest;

@Controller
public class BlogController {
    @RequestMapping("blog")
    public String blogPage(Model model){
        model.addAttribute("bcrumbTitle","Blogs");
        model.addAttribute("bcrumbSubtitle","News");
        model.addAttribute("registerRes", new RegisterRequest());
        return "blog";
    }
}
