package com.tweetapp.resource;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tweetapp.entity.Users;
import com.tweetapp.model.UsersDTO;
import com.tweetapp.service.UserService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserResourceTest {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private UserResource controller;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void newUserRegistrationSuccess() throws Exception {
		Users user = new Users();
		JSONObject json = new JSONObject();
		json.put("loginId", "murugesh");
		json.put("password", "root");
		json.put("email", "murugesan@gmail.com");
		json.put("firstName", "Murugesan");
		json.put("lastName", "D");
		json.put("contactNumber", "123456789");

		when(userService.userRegistration(Mockito.any())).thenReturn(user);

		MockHttpServletResponse response = mockMvc
				.perform(post("/api/v1.0/tweets/register").accept(MediaType.APPLICATION_JSON).content(json.toString())
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(response.getStatus(), 200);
	}

	@Test
	public void newUserRegistrationThrowValidationFailed() throws Exception {
		Users user = new Users();
		JSONObject json = new JSONObject();
		json.put("loginId", "murugesh");
		json.put("email", "murugesan@gmail.com");
		json.put("firstName", "Murugesan");
		json.put("lastName", "D");
		json.put("contactNumber", "123456789");

		when(userService.userRegistration(Mockito.any())).thenReturn(user);

		MockHttpServletResponse response = mockMvc
				.perform(post("/api/v1.0/tweets/register").accept(MediaType.APPLICATION_JSON).content(json.toString())
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(response.getStatus(), 400);

	}

	@Test
	public void getAllUserSuccess() throws Exception {
		UsersDTO user = new UsersDTO();
		user.setFirstName("Murugesan");
		user.setEmail("murugesan@gmail.com");
		user.setPassword("password");
		user.setLastName("D");
		user.setLoginId("murugesh");
		user.setContactNumber("123456789");
		
		List<UsersDTO> allUsers = new ArrayList<>();
	
		allUsers.add(user);

		when(userService.getAllUsers()).thenReturn(allUsers);

		MockHttpServletResponse response = mockMvc
				.perform(get("/api/v1.0/tweets/users/all").accept(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();
		assertEquals(response.getStatus(), 200);
		assertThat(response.getContentAsString(), containsString("murugesan@gmail.com"));

	}

	@Test
	public void searchUserByUserNameSuccess() throws Exception {
		UsersDTO user = new UsersDTO();
		user.setFirstName("Murugesan");
		user.setEmail("murugesan@gmail.com");
		user.setPassword("password");
		user.setLastName("D");
		user.setLoginId("murugesh");
		user.setContactNumber("123456789");
		List<UsersDTO> result = new ArrayList<>();
		result.add(user);
		when(userService.searchUserByUserName("murugesh")).thenReturn(result);
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/v1.0/tweets/user/search/murugesh").accept(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();
		assertEquals(response.getStatus(), 200);
		assertThat(response.getContentAsString(), containsString("murugesan@gmail.com"));

	}
//	@Test
//	public void passwordResetSuccess(){
//		//when(userService.forgotUserPassword(Mockito.anyString(), Mockito.any(Users.class))).thenReturn(null);
//		Mockito.doNothing();
//	}

}
