package com.wrathyboo.client.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wrathyboo.client.entities.CartItem;
import com.wrathyboo.client.entities.Product;
import com.wrathyboo.client.entities.RegisterRequest;
import com.wrathyboo.client.entities.Type;
import com.wrathyboo.client.entities.User;
import com.wrathyboo.client.service.ProductService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final ProductService productService;
	
    @RequestMapping(value = {"home","/"})
    public String home(Model model, Authentication auth){
    	String currentUser = "guest";
    	if (auth != null) {
    		productService.getUserCart(auth, model);
    	}
    	else currentUser = "guest";
    	
    	List<Product> femaleList = productService.getItemsByType(Type.FEMALE);
    	List<Product> maleList = productService.getItemsByType(Type.MALE);
    	List<Product> popularList = productService.getItemsByRating();
    	
    	model.addAttribute("popularList",popularList);
    	model.addAttribute("femaleList",femaleList);
    	model.addAttribute("maleList",maleList);
    	model.addAttribute("registerRes", new RegisterRequest());
    	model.addAttribute("user",currentUser);
    	
    	
    	
        return "index";
    }

    @RequestMapping("contact")
    public String contactPage(Model model, Authentication auth){
    	productService.getUserCart(auth, model);
    	model.addAttribute("registerRes", new RegisterRequest());
        model.addAttribute("bcrumbTitle","Contact us");
        model.addAttribute("bcrumbSubtitle","keep in touch with us");
        return "contact";
    }

    @RequestMapping("about")
    public String aboutPage(Model model, Authentication auth){
    	productService.getUserCart(auth, model);
    	model.addAttribute("registerRes", new RegisterRequest());
        model.addAttribute("bcrumbTitle","About us");
        model.addAttribute("bcrumbSubtitle","Who we are");
        return "about";
    }

    @RequestMapping("faq")
    public String faqPage(Model model, Authentication auth){
    	productService.getUserCart(auth, model);
    	model.addAttribute("registerRes", new RegisterRequest());
        model.addAttribute("bcrumbTitle","F.A.Q");
        model.addAttribute("bcrumbSubtitle","Frequently asked questions");
        return "faq";
    }

    @RequestMapping("test")
    public String test(){
        return "test";
    }
    
    @RequestMapping("error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error-500";
            }
        }
        return "404";
    }
}
