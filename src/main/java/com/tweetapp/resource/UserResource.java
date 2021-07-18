package com.tweetapp.resource;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.entity.Users;
import com.tweetapp.model.UsersDTO;
import com.tweetapp.service.UserService;

@RestController
@CrossOrigin("*")
public class UserResource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

	@Autowired
	UserService userService;

	@GetMapping("/api/v1.0/tweets/{username}/forgot")
	public ResponseEntity<?> forgotPassword(@PathVariable String username, @RequestBody Users user) throws Exception {

		logger.info(username + "used forgotpassword method" + " password changed successfully");
		userService.forgotUserPassword(username, user);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@PostMapping("/api/v1.0/tweets/register")
	public ResponseEntity<?> newUserRegistration(@Valid @RequestBody UsersDTO user) {
		userService.userRegistration(user);
		logger.info(user.getEmail() + " User Registrartion success");
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/api/v1.0/tweets/users/all")
	public ResponseEntity<List<UsersDTO>> getAllUsers() {

		logger.info("Get all users invoked successfully");
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);

	}

	@GetMapping("/api/v1.0/tweets/user/search/{username}")
	public ResponseEntity<?> searchUserByUserName(@PathVariable String username) {

		List<UsersDTO> user = userService.searchUserByUserName(username);
		logger.info(username + " Found");
		return new ResponseEntity<>(user, HttpStatus.OK);

	}

}
