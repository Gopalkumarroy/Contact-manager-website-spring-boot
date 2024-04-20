package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	//method for adding common data to Response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println(" USERNAME " + userName);
		
		//get the user using username email
		
		User 	user = userRepository.getUserByUserName(userName);
		
		System.out.println(" USER " + user);
		
		model.addAttribute("user", user);
				
	}
     
	// Dashboard home
	
	@GetMapping("/index")
	  public String dashboard(Model model, Principal principal) 
	  {
		String userName = principal.getName();
		System.out.println(" USERNAME " + userName);
		
		//get the user using username email
		
		User 	user = userRepository.getUserByUserName(userName);
		
		System.out.println(" USER " + user);
		
		model.addAttribute("user", user);
				
		  return "normal/user_dashboard";
	  }
	
	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title", "Add contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact_form";
	}
	
	//Process and contact form
	
	@PostMapping("/process-contact")
	public String processContact(
			@ModelAttribute Contact contact, 
			@RequestParam("profileImage") MultipartFile file, 
			Principal principal,  HttpSession session) {
		
		try {
		String name = principal.getName();
		
		User user  = this.userRepository.getUserByUserName(name);
		
		
		
		//contact.setUser(user);
		
		//processing and uploading file
		if(file.isEmpty()) {
			//if file is empty then try our message
			System.out.println("file is empty");
		}
		else {
			//upload the file to the folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			
			File saveFile = new ClassPathResource("static/img").getFile();
			
			      Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
          Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image is Uploaded");
		}
		
		user.getContacts().add(contact);
		
		contact.setUser(user);
		
		this.userRepository .save(user);
		
		System.out.println("DATA  " +contact);
		
		System.out.println("Added to databases");
		
		//message added to successfully.................
		session.setAttribute("message", new Message("Your contact is added !! Add more...", "success"));
		}
		catch (Exception e) {
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();
			//error handling for contact added or not...............
			session.setAttribute("message", new Message("Something went wrong !! Try again...", "danger"));
		}
		
		return "normal/add_contact_form";
	}
}
