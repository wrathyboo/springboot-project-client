package com.wrathyboo.client.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.EntityResponse;

import com.google.gson.Gson;
import com.sun.jersey.api.client.GenericType;
import com.wrathyboo.client.entities.Cart;
import com.wrathyboo.client.entities.CartItem;
import com.wrathyboo.client.entities.Category;
import com.wrathyboo.client.entities.Product;
import com.wrathyboo.client.entities.User;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class ProductService {

	public List<Category> getCategories() {
		Gson son = new Gson();
		WebClient client = WebClient.create();
		WebClient.ResponseSpec responseSpec = client.get().uri("http://localhost:8080/api/v1/category")
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<List<Category>> listType = new GenericType<List<Category>>() {
		};
		List<Category> list = son.fromJson(responseBody, listType.getType());
		return list;
	}



	public List<Product> getItems(Integer page) {
		Gson son = new Gson();
		WebClient client = WebClient.create("http://localhost:8080");
		WebClient.ResponseSpec responseSpec = client.get().uri(builder -> builder.path("/api/v1/product")
                .queryParam("page", page)
                .build())
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<List<Product>> listType = new GenericType<List<Product>>() {
		};
		List<Product> list = son.fromJson(responseBody, listType.getType());
		return list;
	}
	
	public Integer getTotalPages(Boolean flag) {
		Gson son = new Gson();
		WebClient client = WebClient.create("http://localhost:8080");
		WebClient.ResponseSpec responseSpec = client.get().uri(builder -> builder.path("/api/v1/product/getPages")
				.queryParam("customSearch", flag)
                .build())
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<Integer> listType = new GenericType<Integer>() {
		};
		return son.fromJson(responseBody, listType.getType());
	}
	
	public List<Product> getFilteredItems(List<Integer> category_ids, Integer min, Integer max, Integer sale, Boolean flag) {
		Gson son = new Gson();
		WebClient client = WebClient.create("http://localhost:8080");
		WebClient.ResponseSpec responseSpec = client.get().uri(builder -> builder.path("/api/v1/product")
                .queryParam("category", category_ids)
                .queryParam("min", min)
                .queryParam("max", max)
                .queryParam("sale", sale)
                .queryParam("customSearch", flag)
                .build())
                .retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<List<Product>> listType = new GenericType<List<Product>>() {
		};
		List<Product> list = son.fromJson(responseBody, listType.getType());
		return list;
	}
	
	public List<CartItem> getCart(Integer id) {
		Gson son = new Gson();
		WebClient client = WebClient.create();
		WebClient.ResponseSpec responseSpec = client.get().uri("http://localhost:8080/api/v1/cart?id={id}",id)
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<List<CartItem>> listType = new GenericType<List<CartItem>>() {
		};
		List<CartItem> list = son.fromJson(responseBody, listType.getType());
		return list;
	}
	
	public Object cartAddItem(Cart request) {
		WebClient client = WebClient.create();
		val response = client.post().uri("http://localhost:8080/api/v1/cart/add")
				.bodyValue(request)
				.retrieve()
				.bodyToMono(Void.class)
				.block();
		return response;
	}
	
	public List<Product> searchItems(String keys) {
		Gson son = new Gson();
		WebClient client = WebClient.create("http://localhost:8080");
		WebClient.ResponseSpec responseSpec = client.get().uri(builder -> builder.path("/api/v1/product")
				                                    .queryParam("search", keys)
				                                    .build())
				                                    .retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<List<Product>> listType = new GenericType<List<Product>>() {
		};
		List<Product> list = son.fromJson(responseBody, listType.getType());
		return list;
	}
	
	public Product getItem(Integer id) {
		Gson son = new Gson();
		WebClient client = WebClient.create();
		WebClient.ResponseSpec responseSpec = client.get().uri("http://localhost:8080/api/v1/product/{id}",id)
				.retrieve();
		String responseBody = responseSpec.bodyToMono(String.class).block();
		GenericType<Product> listType = new GenericType<Product>() {
		};
		Product item = son.fromJson(responseBody, listType.getType());

		return item;
	}


	public void getUserCart(Authentication auth, Model model) {
		if (auth != null) {
    		User user = (User)auth.getPrincipal();
    		List<CartItem> cartList = getCart(user.getId());
            Integer total = 0;
            for (CartItem x : cartList) {
            	total += x.getTotal();
            }
            Integer cartSize = cartList.size();
            model.addAttribute("cartList",cartList);
        	model.addAttribute("total",total);
        	model.addAttribute("cartSize",cartSize);
    	}
	}

	

	
}
