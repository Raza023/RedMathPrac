package com.practice.session1;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class Session1ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	String expectedContentPattern =
			"<!DOCTYPE html>\r\n" +
					"<html lang=\"en\">\r\n" +
					"<head>\r\n" +
					"    <meta charset=\"UTF-8\">\r\n" +
					"    <title>Title</title>\r\n" +
					"</head>\r\n" +
					"<body>";

	@Test
	public void testSession1ApplicationTests() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/index.html"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.content().string(
						Matchers.startsWith(expectedContentPattern)));
	}
}