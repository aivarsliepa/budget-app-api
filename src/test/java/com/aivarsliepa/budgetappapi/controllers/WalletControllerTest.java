package com.aivarsliepa.budgetappapi.controllers;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(WalletController.class)
public class WalletControllerTest {
    // TODO: rewrite as integration test

//    private static final String WALLET_NAME = "some test name";
//    private static final Long WALLET_ID = 2L;
//
//    private static final String BASE_URL = URLPaths.Wallets.BASE;
//    private static final String WALLET_URL = BASE_URL + "/" + WALLET_ID;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ObjectMapper mapper;
//
//    @MockBean
//    private WalletService walletService;
//
//    private WalletData createValidData() {
//        var data = new WalletData();
//        data.setName(WALLET_NAME);
//        data.setId(WALLET_ID);
//        return data;
//    }
//
//    @Test
//    public void getList_works() throws Exception {
//        var data = createValidData();
//        var expectedResponseData = new WalletData[]{createValidData()};
//
//        given(walletService.getList()).willReturn(Collections.singletonList(data));
//
//        var resString = mvc.perform(get(BASE_URL))
//                           .andExpect(status().isOk())
//                           .andReturn()
//                           .getResponse()
//                           .getContentAsString();
//
//        assertArrayEquals(mapper.readValue(resString, WalletData[].class), expectedResponseData);
//    }
//
//    @Test
//    public void create_fails_whenBodyIsEmpty() throws Exception {
//        mvc.perform(post(BASE_URL))
//           .andExpect(status().isBadRequest())
//           .andExpect(content().string(""));
//
//        verifyZeroInteractions(walletService);
//    }
//
//    @Test
//    public void create_returnsData_whenBodyValid() throws Exception {
//        var requestData = createValidData();
//        var responseData = createValidData();
//        var expected = createValidData();
//
//        given(walletService.create(requestData)).willReturn(responseData);
//
//        var resString = mvc.perform(post(BASE_URL)
//                                            .content(mapper.writeValueAsString(requestData))
//                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
//                           .andExpect(status().isOk())
//                           .andReturn()
//                           .getResponse()
//                           .getContentAsString();
//
//        assertEquals(mapper.readValue(resString, WalletData.class), expected);
//    }
//
//    @Test
//    public void getById_returnsNotFoundStatusAndEmptyBody_whenNoMatchingDataFound() throws Exception {
//        given(walletService.findById(WALLET_ID)).willReturn(Optional.empty());
//
//        mvc.perform(get(WALLET_URL))
//           .andExpect(status().isNotFound())
//           .andExpect(content().string(""));
//    }
//
//    @Test
//    public void getById_returnsFoundData_whenIdMatches() throws Exception {
//        var data = createValidData();
//        var expected = createValidData();
//
//        given(walletService.findById(WALLET_ID)).willReturn(Optional.of(data));
//
//        var resString = mvc.perform(get(WALLET_URL))
//                           .andExpect(status().isOk())
//                           .andReturn()
//                           .getResponse()
//                           .getContentAsString();
//
//        assertEquals(mapper.readValue(resString, WalletData.class), expected);
//    }
//
//    @Test
//    public void updateById_returnsStatusNotFound_whenNotFound() throws Exception {
//        var data = createValidData();
//
//        given(walletService.updateById(anyLong(), any(WalletData.class))).willReturn(Optional.empty());
//
//        mvc.perform(post(WALLET_URL)
//                            .content(mapper.writeValueAsString(data))
//                            .contentType(MediaType.APPLICATION_JSON_UTF8))
//           .andExpect(status().isNotFound())
//           .andExpect(content().string(""));
//    }
//
//    @Test
//    public void updateById_returnsStatusBadRequest_whenInvalidBody() throws Exception {
//        var data = new WalletData();
//
//        mvc.perform(post(WALLET_URL)
//                            .content(mapper.writeValueAsString(data))
//                            .contentType(MediaType.APPLICATION_JSON_UTF8))
//           .andExpect(status().isBadRequest())
//           .andExpect(content().string(""));
//
//        verifyZeroInteractions(walletService);
//    }
//
//    @Test
//    public void updateById_returnsUpdatedData_whenValidRequest() throws Exception {
//        var data = createValidData();
//        var expected = createValidData();
//
//        given(walletService.updateById(anyLong(), any(WalletData.class))).willReturn(Optional.of(data));
//
//        var resString = mvc.perform(post(WALLET_URL)
//                                            .content(mapper.writeValueAsString(data))
//                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
//                           .andExpect(status().isOk())
//                           .andReturn()
//                           .getResponse()
//                           .getContentAsString();
//
//        assertEquals(mapper.readValue(resString, WalletData.class), expected);
//    }
//
//    @Test
//    public void deleteById_returnsStatusNotFound_whenNotFound() throws Exception {
//        given(walletService.deleteById(WALLET_ID)).willReturn(false);
//
//        mvc.perform(delete(WALLET_URL))
//           .andExpect(status().isNotFound())
//           .andExpect(content().string(""));
//    }
//
//    @Test
//    public void deleteById_returnsStatusOk_whenFoundAndDeleted() throws Exception {
//        given(walletService.deleteById(WALLET_ID)).willReturn(true);
//
//        mvc.perform(delete(WALLET_URL))
//           .andExpect(status().isOk())
//           .andExpect(content().string(""));
//
//        verify(walletService).deleteById(WALLET_ID);
//    }
}
