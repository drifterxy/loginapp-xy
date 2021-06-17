package com.example.loginapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UsersController {

	@Autowired
	UserRepository userRepository;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/newUser")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		
		return "newuser_form";
	}
	
	@PostMapping("/newUser")
	public String processRegister(User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		userRepository.save(user);
		
		return "redirect:/users";
	}
	
	@GetMapping("/users")
	public String listUsers(Model model) {
		List<User> listUsers = (List<User>) userRepository.findAll();
		model.addAttribute("listUsers", listUsers);
		
		return "users";
	}

}
