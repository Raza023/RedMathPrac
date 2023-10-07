package com.practice.session2;

import com.practice.session2.News.NewsRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;


@SpringBootTest
@AutoConfigureMockMvc
public class Session2ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private NewsRepository newsRepository;

	String expectedContentPattern = "<!DOCTYPE html>";

	@Test
	@WithMockUser(username = "reporter") //, roles = {"ROLE_USER"}) // Set up a mock user
	public void testSession1ApplicationTests() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/index.html"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().string(
						Matchers.startsWith(expectedContentPattern)));
	}


	@Test   //already allowed
//	@WithMockUser(username = "reporter") //, roles = {"ROLE_USER"}) // Set up a mock user
	public void testAllNews() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title 1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].tags").value("tags 1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("title 2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].tags").value("tags 2"));
	}

	@Test
	@WithMockUser(username = "reporter") //, roles = {"ROLE_USER"}) // Set up a mock user
	public void testOneNews() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/1"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title 1"));
	}

	@Test
	@WithMockUser(username = "reporter") //, roles = {"ROLE_USER"}) // Set up a mock user
	public void testOneNewsWithTitle() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/search?title=1"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title 1"));
	}
	@Test
//	@WithMockUser(username = "reporter" , roles = "REPORTER") // Set up a mock user
	public void testInsertNews() throws Exception {
		// Create a sample news object for the request body
		String jsonRequest = "{\"title\":\"title 22\",\"details\":\"details 22\",\"tags\":\"tags 22\",\"reportedTime\":\"2000-01-01T12:00:00\"}";

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/news")
						.with(SecurityMockMvcRequestPostProcessors.user("reporter").authorities(new SimpleGrantedAuthority("REPORTER")))   //we can use this line as well.
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title 22"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("details 22"));
	}

	@Test
//	@WithMockUser(username = "reporter") //, roles = {"ROLE_USER"}) // Set up a mock user
	public void testUpdateNews() throws Exception {
		// Create a sample news object for the request body with updated values
		String jsonRequest = "{\"title\":\"Updated Title\",\"details\":\"Updated Details\",\"tags\":\"Updated Tags\",\"reportedTime\":\"2000-01-01T12:00:00\"}";

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/news/{id}", 1L)  // Provide the ID of the news item to update
						.with(SecurityMockMvcRequestPostProcessors.user("reporter").authorities(new SimpleGrantedAuthority("REPORTER")))   //we can use this line as well.
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("Updated Details"));
	}

	@Test
//	@WithMockUser(username = "reporter", roles = {"REPORTER"}) // Set up a mock user
	public void testDeleteNews() throws Exception {
		// Perform a DELETE request to the /api/v1/news/{id} endpoint, where {id} is the ID of the news item to delete

		// When: Perform a DELETE request to the /api/v1/news/{id} endpoint
		mockMvc.perform(delete("/api/v1/news/{id}", 1L)
						.with(SecurityMockMvcRequestPostProcessors.user("reporter").authorities(new SimpleGrantedAuthority("REPORTER")))
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().is2xxSuccessful());

//		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/news/{id}", 1L))  // Provide the ID of the news item to delete
//				.with(SecurityMockMvcRequestPostProcessors.user("reporter").authorities(new SimpleGrantedAuthority("REPORTER")))   //we can use this line as well.
//				.with(SecurityMockMvcRequestPostProcessors.csrf())
//				.andDo(MockMvcResultHandlers.print())
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

		// Check (assure) that the news item is deleted from the database
		assertFalse(newsRepository.existsById(1L));
	}
}