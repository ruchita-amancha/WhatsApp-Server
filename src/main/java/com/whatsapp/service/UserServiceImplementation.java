package com.whatsapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.whatsapp.config.TokenProvider;
import com.whatsapp.entity.User;
import com.whatsapp.exception.UserException;
import com.whatsapp.repository.UserRepository;
import com.whatsapp.request.UpdateUserRequest;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRespository;

	@Autowired
	private TokenProvider tokenProvider;

	@Override
	public User findUserById(Integer id) throws UserException{
		Optional<User> user=userRespository.findById(id);
		
		if(user.isPresent()) {
			return user.get();
		}
		throw new UserException("User not found with id "+id);
	}

	@Override
	public User findUserProfile(String jwt) throws UserException {
		// Extract the email from the token
		String email = tokenProvider.getEmailFromToken(jwt);

		// Check if the email is valid
		if (email == null || email.isEmpty()) {
			throw new BadCredentialsException("Received invalid token");
		}

		// Use the UserRepository to find the user by email
		User user = userRespository.findByEmail(email);

		// If the user is not found, throw an exception
		if (user == null) {
			throw new UserException("User not found with email: " + email);
		}

		// Return the found user
		return user;
	}


	@Override
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
		User user=findUserById(userId);
		
		if(req.getFull_name()!=null) {
			user.setFull_name(req.getFull_name());
		}
		if(req.getProfile_picture()!=null) {
			user.setProfile_picture(req.getProfile_picture());
		}
		
		return userRespository.save(user);
	}

	@Override
	public List<User> searchUser(String query) {
		List<User> users=userRespository.searchUser(query);
		return users;
	}

}
