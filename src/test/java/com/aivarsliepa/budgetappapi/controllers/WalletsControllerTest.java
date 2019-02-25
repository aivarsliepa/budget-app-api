package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.dto.WalletData;
import com.aivarsliepa.budgetappapi.services.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(WalletsController.class)
public class WalletsControllerTest {

    private final String BASE_URL = "/wallets";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private WalletService walletService;

    @Test
    public void getList_works() throws Exception {
        var name = "test name";
        var id = Long.valueOf(2);

        var data = new WalletData();
        data.setName(name);
        data.setId(id);

        given(walletService.getList()).willReturn(Collections.singletonList(data));

        mvc.perform(get(BASE_URL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].name", is(equalTo(name))))
           .andExpect(jsonPath("$[0].id", is(equalTo(id.intValue()))));
    }

    @Test
    public void create_fails_whenBodyIsEmpty() throws Exception {
        mvc.perform(post(BASE_URL))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(walletService);
    }

    @Test
    public void create_fails_whenBodyIsInvalid() throws Exception {
        var data = mock(WalletData.class);

        mvc.perform(post(BASE_URL, data))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(walletService);
    }

    @Test
    public void create_returnsData_whenBodyValid() throws Exception {
        var name = "test-name";

        var requestData = new WalletData();
        requestData.setName(name);

        var responseData = new WalletData();
        responseData.setName(name);
        responseData.setId(1L);

        given(walletService.create(requestData)).willReturn(responseData);

        var resString = mvc.perform(post(BASE_URL)
                                            .content(mapper.writeValueAsString(requestData))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andExpect(jsonPath("$.name", is(equalTo(name))))
                           .andExpect(jsonPath("$.id", is(equalTo(1))))
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, WalletData.class), responseData);
    }

    @Test
    public void getById_returnsNotFoundStatusAndEmptyBody_whenNoMatchingDataFound() throws Exception {
        var id = 1L;

        given(walletService.findById(id)).willReturn(Optional.empty());

        mvc.perform(get(BASE_URL + "/" + id))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void getById_returnsFoundData_whenIdMatches() throws Exception {
        var name = "test name";
        var id = 1L;

        var data = new WalletData();
        data.setName(name);
        data.setId(1L);

        given(walletService.findById(id)).willReturn(Optional.of(data));

        var resString = mvc.perform(get(BASE_URL + "/" + id))
                           .andExpect(status().isOk())
                           .andExpect(jsonPath("$.name", is(equalTo(name))))
                           .andExpect(jsonPath("$.id", is(equalTo(1))))
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, WalletData.class), data);
    }

    @Test
    public void updateById_returnsStatusNotFound_whenNotFound() throws Exception {
        var name = "test name";
        var id = 1L;

        var data = new WalletData();
        data.setName(name);

        given(walletService.updateById(anyLong(), any(WalletData.class))).willReturn(Optional.empty());

        mvc.perform(post(BASE_URL + "/" + id)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void updateById_returnsStatusBadRequest_whenInvalidBody() throws Exception {
        var data = new WalletData();
        var id = 1L;

        mvc.perform(post(BASE_URL + "/" + id)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void updateById_returnsUpdatedData_whenValidRequest() throws Exception {
        var name = "test name";
        var id = 1L;

        var data = new WalletData();
        data.setName(name);

        given(walletService.updateById(anyLong(), any(WalletData.class))).willReturn(Optional.of(data));

        var resString = mvc.perform(post(BASE_URL + "/" + id)
                                            .content(mapper.writeValueAsString(data))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andExpect(jsonPath("$.name", is(equalTo(name))))
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, WalletData.class), data);
    }

    @Test
    public void deleteById_returnsStatusNotFound_whenNotFound() throws Exception {
        var id = 1L;

        given(walletService.deleteById(id)).willReturn(false);

        mvc.perform(delete(BASE_URL + "/" + id))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void deleteById_returnsStatusOk_whenFoundAndDeleted() throws Exception {
        var id = 1L;

        given(walletService.deleteById(id)).willReturn(true);

        mvc.perform(delete(BASE_URL + "/" + id))
           .andExpect(status().isOk())
           .andExpect(content().string(""));
    }
}
