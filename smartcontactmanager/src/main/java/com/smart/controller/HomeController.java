package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	 private  UserRepository userRepository;
   //handler for home page
	@RequestMapping("/") 
	public String home(Model model) {
    	  model.addAttribute("title", "Home - smart contact manager");
		return "home";
	}
	//handeler for about page
    @RequestMapping("/about") 
 	public String about(Model model) {
     	  model.addAttribute("title", "About - smart contact manager");
 		return "about";
 	}
    //handaler for signup page
    @RequestMapping("/signup") 
 	public String signup(Model model) {
     	  model.addAttribute("title", "Register - smart contact manager");
     	  model.addAttribute("user" , new User());
 		return "signup";
    }
    //handaler for login page
    @GetMapping("/signin")
    public String customLogin(Model model) {
    	model.addAttribute("title", "Login-page");
    	return "login";
    }
    //handler for regestering user
    
    @RequestMapping(value = "/do_register",  method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user,   BindingResult result1,@RequestParam(value="agreement", defaultValue = "false")boolean agreement, Model model, HttpSession session)
    {
    	try {
			
    		if(!agreement) 
        	{
        		System.out.println("You have not agreed the terms and condition!");
        		throw new Exception("You have not agreed the terms and condition!");
        	}
    		if(result1.hasErrors()) 
    		{
    			System.out.println("Error " +result1.toString());
    			model.addAttribute("user", user);
    			return "signup";
    		}
        	
        	user.setRole("ROLE_USER");
        	user.setEnabled(true);
        	user.setImageurl("deafault.png");
        	user.setPassword(passwordEncoder.encode(user.getPassword()));
        	
        	System.out.println(" Agreement " + agreement);
        	System.out.println(" User " + user);
        	
        	this .userRepository.save(user);
        	
        	model.addAttribute("User", new User());
        	
        	session.setAttribute("message", new Message(" Successfully Registered! " ,"alert-success"));
			return "signup" ;
    		
    		
		} 
    	catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("USer", user);
			session.setAttribute("message", new Message("Something went wrong" +e.getMessage() , "alert-danger"));
			return "signup" ;
		}
    	   	
    	
        	
			}
}
    	
    


