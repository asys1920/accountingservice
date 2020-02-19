package com.asys1920.accountingservice;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class AccountingserviceApplicationTests {

/*
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserServiceAdapter userServiceAdapter;
    @InjectMocks
    AccountingService accountingService;

    @Autowired
    private AccountingRepository accountingRepository;

    private String billEndpoint = "/bills";
    private String balanceEndpoint = "/balance";

    private User getUser(long userId) {
        return User.builder()
                .id(userId).userName("Fussballgott")
                .city("Frankfurt").country("Germany")
                .firstName("Alex").lastName("Meier")
                .street("Sesamstraße 12").zipCode("12345")
                .build();
    }

    @DisplayName("Tries to retrieve all already created Bills. " +
			"Should return statuscode ok and no content.")
    @Test
    public void should_GetValidBill_When_RequestIsValid() throws Exception {
        mockMvc.perform(get(billEndpoint))
				.andExpect(content().string("[]"))
                .andExpect(status().isOk());
    }

    @Test
    public void should_CreateValidBill_When_RequestIsValid() throws Exception {
        //given
        long userId = 1L;
        Mockito.when(userServiceAdapter.getUser(userId)).thenReturn(getUser(userId));
        JSONObject body = new JSONObject();
        body.put("value", "500");
        body.put("userId", "" + userId);
        body.put("creationDate", "2022-05-01");
        body.put("paymentDeadlineDate", "2022-05-01");
        // when and then
        mockMvc.perform(post(billEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void should_ReturnBalance_When_Get_ValidRequest() throws Exception {
        mockMvc.perform(get(balanceEndpoint))
                .andExpect(status().isOk());
    }

    @Test
    public void should_ReturnErrorMessage_When_Post_EmptyRequest() throws Exception {
        JSONObject body = new JSONObject();
        mockMvc.perform(post(billEndpoint)
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
        mockMvc.perform(post(billEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }*/
}
