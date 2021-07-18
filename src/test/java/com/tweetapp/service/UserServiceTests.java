package com.tweetapp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tweetapp.entity.Users;
import com.tweetapp.exception.ResourceAlreadyExistsException;
import com.tweetapp.exception.ResourceNotFoundException;
import com.tweetapp.model.UsersDTO;
import com.tweetapp.repository.UserRepo;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceTests {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserRepo userRegistration;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private MongoTemplate mongoTemplate;

	@Test(expected = ResourceAlreadyExistsException.class)
	public void userAlreadyExistsException() {
		UsersDTO user = new UsersDTO();
		user.setEmail("test@gmail.com");
		user.setPassword("password");
		Users users = new Users();
		when(userRegistration.findByEmail(Mockito.anyString())).thenReturn(users);
		userService.userRegistration(user);

	}

	@Test
	public void userRegistrationSuccess() {

		UsersDTO user = new UsersDTO();
		user.setEmail("test@gmail.com");
		user.setPassword("password");
		Users users = new Users();
		users.setPassword("password");
		when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("password");
		when(userRegistration.save(Mockito.any())).thenReturn(users);
		Users result = userService.userRegistration(user);
		assertEquals(result.getPassword(), users.getPassword());
	}

	@Test(expected = UsernameNotFoundException.class)
	public void shouldThrowUserNameNotFoundException() {

		when(userRegistration.findByEmail(Mockito.anyString())).thenReturn(null);

		userService.loadUserByUsername(Mockito.anyString());
	}

	@Test
	public void shouldReturnUserDetails() {
		Users users = new Users();
		users.setPassword("password");
		users.setEmail("test@gmail.com");
		when(userRegistration.findByEmail(Mockito.anyString())).thenReturn(users);
		assertEquals(userService.loadUserByUsername(Mockito.anyString()).getUsername(), "test@gmail.com");
	}

	@Test
	public void getDetailsByEmailSucess() {
		Users user = new Users();
		user.setFirstName("Murugesan");
		when(userRegistration.findByEmail(Mockito.anyString())).thenReturn(user);
		assertEquals(userService.getDetailsByEmail(Mockito.anyString()).getFirstName(), "Murugesan");

	}

	@Test(expected = ResourceNotFoundException.class)
	public void getDetailsByEmailResourceNotFoundExp() {
		Users user = new Users();
		user.setFirstName("Murugesan");
		when(userRegistration.findByEmail(Mockito.anyString())).thenThrow(ResourceNotFoundException.class);
		userService.getDetailsByEmail(Mockito.anyString());
	}

	@Test
	public void forgotUserPasswordSuccess() throws Exception {
		Users user = new Users();
		user.setLoginId("murugesan");
		user.setPassword("password");
		when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("password");
		userService.forgotUserPassword(Mockito.anyString(), user);
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void forgotUserPasswordThrowsNotFoundEx() throws Exception{
		Users user = new Users();
		user.setLoginId("murugesan");
		user.setPassword("password");
		when(mongoTemplate.updateFirst(Mockito.any(), Mockito.any(), Mockito.eq(Users.class))).thenThrow(ResourceNotFoundException.class);
		when(bCryptPasswordEncoder.encode(Mockito.anyString())).thenReturn("password");
		userService.forgotUserPassword(Mockito.anyString(), user);
		
	}

	@Test
	public void findUserByLoginIdSuccess() {
		when(userRegistration.findById(Mockito.anyString())).thenReturn(null);
		assertEquals(userService.findUserByLoginId(Mockito.anyString()), null);
	}
	
	@Test
	public void searchUserByUserNameSuccess(){
		Users user = new Users();
	    List<Users> users = new ArrayList<>();
		user.setFirstName("Murugesan");
		users.add(user);
		when(mongoTemplate.find(Mockito.any(),Mockito.eq(Users.class))).thenReturn(users);
		List<UsersDTO> result = userService.searchUserByUserName("Murugesan");
		assertEquals(result.get(0).getFirstName(),"Murugesan");
	}
	
	@Test(expected = ResourceNotFoundException.class)
	public void searchUserByUserNameThrowException(){
		when(mongoTemplate.find(Mockito.any(),Mockito.eq(Users.class))).thenThrow(ResourceNotFoundException.class);
		userService.searchUserByUserName("Murugesan");
	}
	@Test
	public void getAllUsers(){
		Users user = new Users();
		Users user1 = new Users();
		user.setFirstName("Murugesan");
		user1.setFirstName("Murugesh");
		List<Users> result = new ArrayList<>();
		result.add(user);
		result.add(user1);
		when(mongoTemplate.find(Mockito.any(), Mockito.eq(Users.class))).thenReturn(result);
		assertEquals(userService.getAllUsers().get(0).getFirstName(),"Murugesan");
		
	}
		
	}

