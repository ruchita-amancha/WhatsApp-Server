package com.whatsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.whatsapp.config.TokenProvider;
import com.whatsapp.entity.User;
import com.whatsapp.exception.UserException;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.LoginRequest;
import com.whatsapp.response.AuthResponse;
import com.whatsapp.service.CustomUserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private CustomUserService customUserService;

	@PostMapping(value="/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
		String full_name = user.getFull_name();
		String email = user.getEmail();
		String password = user.getPassword();

		User isUser = userRepository.findByEmail(email);
		if (isUser != null) {
			throw new UserException("Email is used with another account " + email);
		}

		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setFull_name(full_name);
		createdUser.setPassword(passwordEncoder.encode(password));
		userRepository.save(createdUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		AuthResponse res = new AuthResponse(jwt, true);
		return new ResponseEntity<AuthResponse>(res, HttpStatus.CREATED);

	}
	@PostMapping(value="/signin")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) {

		String email=req.getEmail();
		String password=req.getPassword();
		
		Authentication authentication=authenticate(email,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		AuthResponse res = new AuthResponse(jwt, true);
		return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED);


	}

	public Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid Username");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password");

		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
