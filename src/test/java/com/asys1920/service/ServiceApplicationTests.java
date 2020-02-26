package com.asys1920.service;

import com.asys1920.model.Bill;
import com.asys1920.model.User;
import com.asys1920.service.adapter.UserServiceAdapter;
import com.asys1920.service.repository.AccountingRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.Period;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountingRepository repository;
    @MockBean
    private UserServiceAdapter userServiceAdapter;

    String billEndpoint = "/bills";
    String balanceEndpoint = "/balance";

    @Test
    void contextLoads() {
    }

    /* GET balance */
    @Test
    public void should_ReturnValidBalance() throws Exception {
        mockMvc.perform(get(balanceEndpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void should_ReturnValidBalance_whenRequest_With_Start() throws Exception {
        JSONObject body = new JSONObject();
        mockMvc.perform(get(balanceEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .param("start", Instant.ofEpochMilli(0).toString())
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void should_ReturnValidBalance_whenRequest_With_End() throws Exception {
        JSONObject body = new JSONObject();
        mockMvc.perform(get(balanceEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .param("end", Instant.now().toString())
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void should_ReturnValidBalance_whenRequest_With_StartAndEnd() throws Exception {
        JSONObject body = new JSONObject();
        mockMvc.perform(get(balanceEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .param("start", Instant.ofEpochMilli(0).toString())
                .param("end", Instant.now().toString())
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void should_ReturnErrorMessage_whenStartIsAfterEnd() throws Exception {
        JSONObject body = new JSONObject();
        mockMvc.perform(get(balanceEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .param("start", Instant.now().toString())
                .param("end", Instant.ofEpochMilli(0).toString())
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    /* GET bills */
    @Test
    public void should_ReturnValidBills() throws Exception {
        mockMvc.perform(get(billEndpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    /* POST bills */
    @Test
    public void should_ReturnValidBill_When_CreatingValidBill() throws Exception {
        Bill validBill = getValidBill();
        JSONObject body = jsonFromBill(validBill);
        Mockito.when(userServiceAdapter.getUser(validBill.getUserId())).thenReturn(createUser());
        Mockito.when(repository.save(Mockito.any(Bill.class))).thenReturn(validBill);
        mockMvc.perform(post(billEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.value").value(body.get("value")))
                .andExpect(jsonPath("$.userId").value(body.get("userId")))
                .andExpect(jsonPath("$.creationDate").value(body.get("creationDate")))
                .andExpect(jsonPath("$.paymentDeadlineDate").value(body.get("paymentDeadlineDate")))
                .andReturn();
    }

    @Test
    public void should_ErrorMessage_When_CreatingInvalidBill_CreationDate() throws Exception {
        JSONObject body = jsonFromBill(getValidBill());
        body.put("creationDate", "asdasd");
        mockMvc.perform(post(billEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void should_ErrorMessage_When_CreatingInvalidBill_PaymentDeadlineDate() throws Exception {
        JSONObject body = jsonFromBill(getValidBill());
        body.put("paymentDeadlineDate", "asdasd");
        mockMvc.perform(post(billEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    /* GET bills/{id} */
    @Test
    public void should_ReturnValidBills_When_ValidId() throws Exception {
        mockMvc.perform(get(billEndpoint)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void should_ReturnErrorMessage_When_InvalidId() throws Exception {
        mockMvc.perform(get(billEndpoint + "/" + getRandomId() * 20)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    /* PATCH bills/{id} */

    @Test
    public void should_ReturnValidBill_When_UpdatingValidBill() throws Exception {
        Bill validBill = getValidBill();
        JSONObject body = jsonFromBill(validBill);
        Mockito.when(userServiceAdapter.getUser(validBill.getUserId())).thenReturn(createUser());
        Mockito.when(repository.save(Mockito.any(Bill.class))).thenReturn(validBill);
        mockMvc.perform(patch(billEndpoint + "/" + body.getString("id"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.value").value(body.get("value")))
                .andExpect(jsonPath("$.userId").value(body.get("userId")))
                .andExpect(jsonPath("$.creationDate").value(body.get("creationDate")))
                .andExpect(jsonPath("$.paymentDeadlineDate").value(body.get("paymentDeadlineDate")))
                .andReturn();
    }

    @Test
    public void should_ErrorMessage_When_UpdatingInvalidBill_CreationDate() throws Exception {
        JSONObject body = jsonFromBill(getValidBill());
        body.put("creationDate", "asdasd");
        mockMvc.perform(patch(billEndpoint + "/" + body.getString("id"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void should_ErrorMessage_When_UpdatingInvalidBill_PaymentDeadlineDate() throws Exception {
        JSONObject body = jsonFromBill(getValidBill());
        body.put("paymentDeadlineDate", "asdasd");
        mockMvc.perform(patch(billEndpoint + "/" + body.getString("id"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    private long getRandomId() {
        return (long) (Math.random() * Integer.MAX_VALUE);
    }

    private JSONObject jsonFromBill(Bill bill) throws JSONException {
        JSONObject jay = new JSONObject();
        jay.put("id", bill.getId());
        jay.put("value", bill.getValue());
        jay.put("userId", bill.getUserId());
        jay.put("creationDate", bill.getCreationDate().toString());
        jay.put("paymentDeadlineDate", bill.getPaymentDeadlineDate().toString());
        jay.put("city", bill.getCity());
        jay.put("country", bill.getCountry());
        jay.put("name", bill.getName());
        jay.put("zipCode", bill.getZipCode());
        return jay;
    }

    private Bill getValidBill() throws JSONException {
        Bill bill = new Bill();
        User user = createUser();
        bill.setValue(200.0);
        bill.setId(getRandomId());
        bill.setUserId(user.getId());
        bill.setCity(user.getCity());
        bill.setCountry(user.getCountry());
        bill.setName(user.getName());
        bill.setZipCode(user.getZipCode());
        bill.setCreationDate(Instant.now());
        bill.setPaymentDeadlineDate(Instant.now().plus(Period.ofDays(90)));
        repository.save(bill);
        return bill;
    }

    private JSONObject getValidUser() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("firstName", "Alexander");
        body.put("lastName", "Meier");
        body.put("userName", "Fussballgott");
        body.put("emailAddress", "a@b.c");
        body.put("expirationDateDriversLicense", Instant.now().toString());
        body.put("street", "Mörfelder Landstraße 362");
        body.put("zipCode", "60528");
        body.put("city", "Frankfurt am Main");
        body.put("country", "Germany");
        return body;
    }

    private User createUser() throws JSONException {
        long userId = 1L;
        User user = new User();
        JSONObject validUser = getValidUser();
        user.setId(userId);
        user.setFirstName(validUser.getString("firstName"));
        user.setLastName(validUser.getString("lastName"));
        user.setUserName(validUser.getString("userName"));
        user.setEmailAddress(validUser.getString("emailAddress"));
        user.setExpirationDateDriversLicense(Instant.now());
        return user;
    }
}
