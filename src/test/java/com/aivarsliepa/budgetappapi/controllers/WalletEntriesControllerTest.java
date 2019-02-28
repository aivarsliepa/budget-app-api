package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.services.WalletEntryService;
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
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WalletEntriesController.class)
public class WalletEntriesControllerTest {

    private static final CategoryType TYPE = CategoryType.INCOME;
    private static final Date DATE = new Date();
    private static final Long CATEGORY_ID = 3L;
    private static final Long WALLET_ID = 1L;
    private static final Long ENTRY_ID = 2L;


    private static final String BASE_URL = URLPaths.Wallets.BASE + "/" + WALLET_ID + URLPaths.Wallets.ENTRIES + "/";
    private static final String ENTRY_URL =
            URLPaths.Wallets.BASE + "/" + WALLET_ID + URLPaths.Wallets.ENTRIES + "/" + ENTRY_ID;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private WalletEntryService walletEntryService;

    private WalletEntryData createValidData() {
        var data = new WalletEntryData();
        data.setCategoryId(CATEGORY_ID);
        data.setType(TYPE);
        data.setDate(DATE);
        return data;
    }

    @Test
    public void getListByWalletId_test() throws Exception {
        var data = createValidData();
        var expectedResponseData = new WalletEntryData[]{data};

        given(walletEntryService.getListByWalletId(WALLET_ID)).willReturn(Collections.singletonList(data));

        var resString = mvc.perform(get(BASE_URL))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertArrayEquals(mapper.readValue(resString, WalletEntryData[].class), expectedResponseData);
    }

    @Test
    public void createEntryToWallet_fails_whenBodyIsInvalid() throws Exception {
        var data = new WalletEntryData();

        mvc.perform(post(BASE_URL)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(walletEntryService);
    }


    @Test
    public void createEntryToWallet_returnsBadRequestStatus_whenWalletEntryServiceReturnsEmptyOptional() throws Exception {
        var requestData = createValidData();

        given(walletEntryService.createEntryToWallet(eq(WALLET_ID), any(WalletEntryData.class))).willReturn(Optional.empty());

        mvc.perform(post(BASE_URL)
                            .content(mapper.writeValueAsString(requestData))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verify(walletEntryService).createEntryToWallet(eq(WALLET_ID), any(WalletEntryData.class));
    }

    @Test
    public void createEntryToWallet_returnsData_whenWalletEntryServiceReturnsData() throws Exception {
        var requestData = createValidData();
        var responseData = createValidData();
        var expected = createValidData();

        given(walletEntryService.createEntryToWallet(WALLET_ID, requestData)).willReturn(Optional.of(responseData));

        var resString = mvc.perform(post(BASE_URL)
                                            .content(mapper.writeValueAsString(requestData))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, WalletEntryData.class), expected);
    }

    @Test
    public void findByIdAndWalletId_returnsNotFoundStatusAndEmptyBody_whenMatchingDataNotFound() throws Exception {
        given(walletEntryService.findByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(Optional.empty());

        mvc.perform(get(ENTRY_URL))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void findByIdAndWalletId_returnsFoundData_whenMatchingDataFound() throws Exception {
        var expected = createValidData();
        var data = createValidData();

        given(walletEntryService.findByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(Optional.of(data));

        var resString = mvc.perform(get(ENTRY_URL))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, WalletEntryData.class), expected);
    }

    @Test
    public void updateByIdAndWalletId_returnsStatusNotFound_whenNotFound() throws Exception {
        var data = createValidData();

        given(walletEntryService.updateByIdAndWalletId(anyLong(), anyLong(), any(WalletEntryData.class)))
                .willReturn(Optional.empty());

        mvc.perform(post(ENTRY_URL)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void updateByIdAndWalletId_returnsStatusBadRequest_whenInvalidBody() throws Exception {
        var data = new WalletEntryData();

        mvc.perform(post(ENTRY_URL)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(walletEntryService);
    }

    @Test
    public void updateByIdAndWalletId_returnsUpdatedData_whenValidRequest() throws Exception {
        var data = createValidData();
        var expected = createValidData();

        given(walletEntryService.updateByIdAndWalletId(ENTRY_ID, WALLET_ID, data))
                .willReturn(Optional.of(data));

        var resString = mvc.perform(post(ENTRY_URL)
                                            .content(mapper.writeValueAsString(data))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, WalletEntryData.class), expected);
    }

    @Test
    public void deleteByIdAndWalletId_returnsStatusNotFound_whenNotFound() throws Exception {
        given(walletEntryService.deleteByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(false);

        mvc.perform(delete(ENTRY_URL))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void deleteByIdAndWalletId_returnsStatusOk_whenFoundAndDeleted() throws Exception {
        given(walletEntryService.deleteByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(true);

        mvc.perform(delete(ENTRY_URL))
           .andExpect(status().isOk())
           .andExpect(content().string(""));

        verify(walletEntryService).deleteByIdAndWalletId(ENTRY_ID, WALLET_ID);
    }
}
