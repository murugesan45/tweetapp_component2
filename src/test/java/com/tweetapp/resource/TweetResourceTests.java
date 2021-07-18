package com.tweetapp.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
import com.tweetapp.service.UserService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TweetResourceTests {

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
}
