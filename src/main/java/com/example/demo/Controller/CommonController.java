package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Entities.UsersEntity;
import com.example.demo.Service.UserService;

@RestController
@RequestMapping("/user")
public class CommonController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/byNumber")
	public UsersEntity getUserbyMobile(@RequestParam String phoneNumber) {
	    UsersEntity user = userService.findByPhoneNumber(phoneNumber);
	    if (user == null) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
	    }
	   
	    return user;
	}

}
