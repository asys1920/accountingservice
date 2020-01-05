package com.asys1920.accountingservice;

import com.asys1920.accountingservice.repository.AccountingRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountingserviceApplicationTests {


	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountingRepository accountingRepository;

	private String endpoint = "/bills";

	@Test
	public void should_ReturnValid_When_Get_ValidRequest() throws Exception {
		mockMvc.perform(get(endpoint))
				.andExpect(status().isOk());
	}

	@Test
	public void should_ReturnValid_When_Post_ValidRequest() throws Exception {
		JSONObject body = new JSONObject();
		body.put("value", "500");
		body.put("userId", "5");
		body.put("creationDate", "2022-05-01");
		body.put("paymentDeadlineDate", "2022-05-01");
		mockMvc.perform(post(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	

	@Test
	public void should_ReturnErrorMessage_When_Post_EmptyRequest() throws Exception {
		JSONObject body = new JSONObject();
		mockMvc.perform(post(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void should_ReturnErrorMessage_When_Post_InvalidDate() throws Exception {
		JSONObject body = new JSONObject();
		body.put("value", "500");
		body.put("userId", "5");
		body.put("creationDate", "2022-05-01");
		body.put("paymentDeadlineDate", "2022-05-A");
		mockMvc.perform(post(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(body.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
