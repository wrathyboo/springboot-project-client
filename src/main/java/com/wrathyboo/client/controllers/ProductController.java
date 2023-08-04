package com.wrathyboo.client.controllers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wrathyboo.client.entities.Cart;
import com.wrathyboo.client.entities.CartItem;
import com.wrathyboo.client.entities.Category;
import com.wrathyboo.client.entities.Product;
import com.wrathyboo.client.entities.RegisterRequest;
import com.wrathyboo.client.entities.User;
import com.wrathyboo.client.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService; 
	
    @RequestMapping("category")
    public String categoryPage(Model model,
    		                   @RequestParam(name="search", required=false) String keywords,
    		                   @RequestParam(name="priceInput", required=false) String price,
    		                   @RequestParam(name="page", defaultValue = "0", required=false) Integer page,
    		                   @ModelAttribute(name="filteredList") ArrayList<Product> filteredList,
    		                   Authentication auth){
    	
    	List<Product> list = productService.getItems(page == 0 ? page : page - 1);
    	List<Category> categories = productService.getCategories();
    	Integer totalPages = productService.getTotalPages(true);

    	if (keywords != null) {
            list = productService.searchItems(keywords);
    	}
    	
    	if (filteredList != null) {
    		if (filteredList.isEmpty()) {
    			model.addAttribute("noProducts","No product found!");
    		}
    		else {
    			list = filteredList;
    		}
    	}
    	
    	System.out.println("Total pages: " + productService.getTotalPages(true));
    	
    	productService.getUserCart(auth, model);
    	
    	model.addAttribute("categories",categories);
    	model.addAttribute("list",list);
    	model.addAttribute("totalPages",totalPages);
    	model.addAttribute("currentPage",page + 1);
        model.addAttribute("bcrumbTitle","Shop");
        model.addAttribute("bcrumbSubtitle","Trend");
        model.addAttribute("registerRes", new RegisterRequest());
        return "category";
    }
    @PostMapping("category")
    public String customFilter(Model model,
    		                   @RequestParam(name="rangeMin", defaultValue = "0", required=false) Integer rangeMin,
    		                   @RequestParam(name="rangeMax", defaultValue = "500", required=false) Integer rangeMax,
    		                   @RequestParam(name="categories",defaultValue = "", required = false) List<Integer> cat,
    		                   @RequestParam(name="gender", defaultValue = "UNISEX", required = false) String type,
    		                   @RequestParam(name="sale", defaultValue = "0", required = false) Integer sale,
    		                   final RedirectAttributes redirectAttributes,
    		                   Authentication auth){
 
    		   System.out.println(rangeMin);
    		   System.out.println(rangeMax);

 
    	   System.out.println(type);

 
    	   System.out.println(sale);
      
           Boolean flag = true;
    	   List<Product> list = productService.getFilteredItems(cat, rangeMin * 100, rangeMax * 100, sale, flag);
    	   
    	 
    	   redirectAttributes.addFlashAttribute("filteredList",list);
       
       
        model.addAttribute("bcrumbTitle","Shop");
        model.addAttribute("bcrumbSubtitle","Trend");
        model.addAttribute("registerRes", new RegisterRequest());
        return "redirect:category";
    }
    @RequestMapping("product/{id}")
    public String pubductPage(@PathVariable Integer id, Model model, Authentication auth){
    	productService.getUserCart(auth, model);
    	Product p = productService.getItem(id);
    	model.addAttribute("p",p);
    	model.addAttribute("registerRes", new RegisterRequest());
        return "product";
    }

    @RequestMapping("cart")
    public String cartPage(Model model, Authentication auth){
    	User user = (User)auth.getPrincipal();
    	List<CartItem> list = productService.getCart(user.getId());
        Integer total = 0;
        for (CartItem x : list) {
        	total += x.getTotal();
        }
        Integer cartSize = list.size();
    	model.addAttribute("list",list);
    	model.addAttribute("total",total);
    	model.addAttribute("cartSize",cartSize);
    	model.addAttribute("registerRes", new RegisterRequest());
        model.addAttribute("bcrumbTitle","My Cart");
        model.addAttribute("bcrumbSubtitle","Shop");
        return "cart";
    }
    
    @PostMapping("add-to-cart/{id}")
    public String cartAddItem(@PathVariable Integer id, Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = (User)auth.getPrincipal();
    	Cart cart = new Cart();
    	cart.setOwner(user.getId());
    	cart.setItem(id);
    	cart.setQuantity(1);
    	productService.cartAddItem(cart);
    	return "redirect:../category";
    }

    @RequestMapping("checkout")
    public String checkoutPage(Model model, Authentication auth){
    	productService.getUserCart(auth, model);
    	model.addAttribute("registerRes", new RegisterRequest());
        model.addAttribute("bcrumbTitle","Checkout");
        model.addAttribute("bcrumbSubtitle","Shop");
        return "checkout";
    }

    @RequestMapping("wishlist")
    public String wishlistPage(Model model){
    	model.addAttribute("registerRes", new RegisterRequest());
        model.addAttribute("bcrumbTitle","Wishlist");
        model.addAttribute("bcrumbSubtitle","Shop");
        return "wishlist";
    }
}
