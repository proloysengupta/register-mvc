package com.proloy.demo;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proloy.demo.model.User;
import com.proloy.demo.model.UserRepo;

@RestController
public class RegsitrationController {

	@Autowired
	private UserRepo userRepo;

	@RequestMapping("/register-user/{userName}/{email}/{password}")
	public String registerUser(@PathVariable("userName") String userName, @PathVariable("email") String email,
			@PathVariable("password") String password) {

		Encoder encoder = Base64.getEncoder();
		String originalString = password;
		String encodedString = encoder.encodeToString(originalString.getBytes());

		User u = new User();
		u.setName(userName);
		u.setEmail(email);
		u.setPassword(encodedString);
		userRepo.save(u);
		return "Successfully registered !!!";
	}

	@RequestMapping("/changepassword-user/{userName}/{email}/{password1}/{password2}")
	public String changePassword(@PathVariable("userName") String userName, @PathVariable("email") String email,
			@PathVariable("password1") String password1, @PathVariable("password2") String password2) {
		User u = null;
		try {
			u = userRepo.findByName(userName);
		} catch (Exception e) {
			System.out.println("User not found !!!");
		}
		if (u != null) {
			Decoder decoder = Base64.getDecoder();
			byte[] bytes = decoder.decode(u.getPassword());
			String newPass = new String(bytes);

			if (newPass.equals(password1)) {
				Encoder encoder = Base64.getEncoder();
				String originalString = password2;
				String encodedString = encoder.encodeToString(originalString.getBytes());
				u.setPassword(encodedString);
				userRepo.save(u);
			}
		}
		return "Password changed !!!";
	}

	@RequestMapping("/changeDetails-user/{userName1}/{userName}/{email}/{password1}/{password2}")
	public String changeDetails(@PathVariable("userName1") String userName1, @PathVariable("userName") String userName,
			@PathVariable("email") String email, @PathVariable("password1") String password1,
			@PathVariable("password2") String password2) {
		User u = null;
		try {
			u = userRepo.findByName(userName1);
		} catch (Exception e) {
			System.out.println("User not found !!!");
		}
		if (u != null) {
			u.setName(userName);
			u.setEmail(email);
			userRepo.save(u);
		}
		return "Details updated !!!";
	}
}
