package com.tweetapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.entity.Users;
import com.tweetapp.exception.ResourceAlreadyExistsException;
import com.tweetapp.exception.ResourceNotFoundException;
import com.tweetapp.model.UsersDTO;
import com.tweetapp.repository.UserRepo;

@Service
public class UserService implements UsersService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UserRepo userRegistration;

	@Autowired
	MongoTemplate mongoTemplate;

	public void userService(BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	public Users userRegistration(UsersDTO data){

		ModelMapper modelMapper = new ModelMapper();
		
		Users userData = modelMapper.map(data, Users.class);

		Users user = userRegistration.findByEmail(userData.getEmail());
		if (user != null) {
			throw new ResourceAlreadyExistsException("Already Exists");
		}
		userData.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
		
		return userRegistration.save(userData);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		Users user = userRegistration.findByEmail(userName);
		if (user == null)
			throw new UsernameNotFoundException(userName);
		return new User(user.getEmail(), user.getPassword(), true, true, true, true, new ArrayList<>());

	}

	public Users getDetailsByEmail(String userName) {
			Users user = userRegistration.findByEmail(userName);
			if(user == null)
				throw new ResourceNotFoundException("Resource Not Found");
			return user;
	}

	public void forgotUserPassword(String username, Users user) throws Exception {

		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("loginId").is(username));
			Update update = new Update();
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			update.set("password", user.getPassword());
			mongoTemplate.updateFirst(query, update, Users.class);
		} catch (Exception e) {

			throw new ResourceNotFoundException("The loginId" + username + "Not found");

		} 

	}

	public Optional<Users> findUserByLoginId(String username) {

		return  userRegistration.findById(username);
		

	}

	public List<UsersDTO> searchUserByUserName(String userName) {

		Query query = new Query();
		query.addCriteria(Criteria.where("loginId").regex("^" + userName));
		query.fields().exclude("password");
		query.fields().exclude("contactNumber");
		List<Users> users = mongoTemplate.find(query, Users.class);

		ModelMapper modelMapper = new ModelMapper();
		List<UsersDTO> result = users.stream().map(user -> modelMapper.map(user, UsersDTO.class))
				.collect(Collectors.toList());

		return result;
		

	}

	public List<UsersDTO> getAllUsers() {
		Query query = new Query();
		query.fields().exclude("password");
		query.fields().exclude("contactNumber");
		List<Users> user = mongoTemplate.find(query, Users.class);
		if(user == null)
			throw new ResourceNotFoundException();

		ModelMapper modelMapper = new ModelMapper();

		List<UsersDTO> result = user.stream().map(users -> modelMapper.map(users, UsersDTO.class))
				.collect(Collectors.toList());

		return result;

	}

}
