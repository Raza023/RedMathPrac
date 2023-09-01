package com.practice.session2;

import com.practice.session2.core.News;
import com.practice.session2.core.NewsController;
import com.practice.session2.core.NewsService;
import com.practice.session2.core.NewsRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public class Session2ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private NewsRepository newsRepository;

	String expectedContentPattern = "<!DOCTYPE html>";

	@Test
	public void testSession1ApplicationTests() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/index.html"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().string(
						Matchers.startsWith(expectedContentPattern)));
	}


	@Test
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
	public void testOneNews() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/1"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title 1"));
	}

	@Test
	public void testOneNewsWithTitle() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/news/search?title=t"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title 1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("title 2"));
	}
	@Test
	public void testInsertNews() throws Exception {
		// Create a sample news object for the request body
		String jsonRequest = "{\"title\":\"title 22\",\"details\":\"details 22\",\"tags\":\"tags 22\",\"reportedTime\":\"2000-01-01T12:00:00\"}";

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/news")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title 22"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("details 22"));
	}

	@Test
	public void testUpdateNews() throws Exception {
		// Create a sample news object for the request body with updated values
		String jsonRequest = "{\"title\":\"Updated Title\",\"details\":\"Updated Details\",\"tags\":\"Updated Tags\",\"reportedTime\":\"2000-01-01T12:00:00\"}";

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/news/{id}", 1L)  // Provide the ID of the news item to update
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonRequest))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.details").value("Updated Details"));
	}

	@Test
	public void testDeleteNews() throws Exception {
		// Perform a DELETE request to the /api/v1/news/{id} endpoint, where {id} is the ID of the news item to delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/news/{id}", 1L))  // Provide the ID of the news item to delete
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

		// Check that the news item is deleted from the database
		assertFalse(newsRepository.existsById(1L));
	}
}