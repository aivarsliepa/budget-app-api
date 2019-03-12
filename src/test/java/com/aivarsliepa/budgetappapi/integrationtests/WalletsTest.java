package com.aivarsliepa.budgetappapi.integrationtests;


import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.user.UserModel;
import com.aivarsliepa.budgetappapi.data.user.UserRepository;
import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import com.aivarsliepa.budgetappapi.data.wallet.WalletRepository;
import com.aivarsliepa.budgetappapi.security.JwtConfig;
import com.aivarsliepa.budgetappapi.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class WalletsTest {
    private static final String WALLETS_URL = URLPaths.Wallets.BASE;

    private static final String USERNAME_1 = "username_1";
    private static final String USERNAME_2 = "username_2";
    private static final String PASSWORD_1 = "password_1";
    private static final String PASSWORD_2 = "password_2";
    private static final String NAME_1 = "name_1";
    private static final String NAME_2 = "name_2";
    private static final String NAME_3 = "name_3";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtConfig jwtConfig;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        userRepository.deleteAll();
        walletRepository.deleteAll();
    }

    private Long createUserOne() {
        var user = new UserModel();
        user.setPassword(passwordEncoder.encode(PASSWORD_1));
        user.setUsername(USERNAME_1);
        return userRepository.save(user).getId();
    }

    private Long createUserTwo() {
        var user = new UserModel();
        user.setPassword(passwordEncoder.encode(PASSWORD_2));
        user.setUsername(USERNAME_2);
        return userRepository.save(user).getId();
    }

    private Long createWallet(Long userId, String name) {
        var wallet2 = new WalletModel();
        wallet2.setUserId(userId);
        wallet2.setName(name);
        return walletRepository.save(wallet2).getId();
    }

    private WalletModel mustFindWallet(Long id) {
        var modelOpt = walletRepository.findById(id);

        if (modelOpt.isEmpty()) {
            fail("Model should not be empty!");
        }

        return modelOpt.get();
    }

    @Test
    public void get_mustHaveAuth() throws Exception {
        mvc.perform(get(WALLETS_URL))
           .andExpect(status().isUnauthorized())
           .andExpect(content().string(""));
    }

    @Test
    public void get_happyPath() throws Exception {
        var userId1 = createUserOne();
        var userId2 = createUserTwo();
        var walletId1 = createWallet(userId1, NAME_1);
        var walletId2 = createWallet(userId1, NAME_2);
        createWallet(userId2, NAME_3);
        var token = jwtTokenProvider.generateTokenFromId(userId1);

        var request = get(WALLETS_URL).header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        var resString = mvc.perform(request)
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        var list = Arrays.asList(mapper.readValue(resString, WalletData[].class));

        var expected1 = new WalletData();
        expected1.setId(walletId1);
        expected1.setName(NAME_1);

        var expected2 = new WalletData();
        expected2.setId(walletId2);
        expected2.setName(NAME_2);

        assertEquals(2, list.size());
        assertThat(list, containsInAnyOrder(expected1, expected2));
    }

    @Test
    public void create_mustHaveAuth() throws Exception {
        mvc.perform(post(WALLETS_URL))
           .andExpect(status().isUnauthorized())
           .andExpect(content().string(""));
    }

    @Test
    public void create_happyPath() throws Exception {
        var userId = createUserOne();
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var requestBody = new WalletData();
        requestBody.setName(NAME_1);

        var request = post(WALLETS_URL).content(mapper.writeValueAsString(requestBody))
                                       .contentType(MediaType.APPLICATION_JSON_UTF8)
                                       .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        var response = mvc.perform(request)
                          .andExpect(status().isOk())
                          .andReturn()
                          .getResponse()
                          .getContentAsString();

        var responseBody = mapper.readValue(response, WalletData.class);
        assertEquals(NAME_1, responseBody.getName());

        // verify correct user relation
        var wallet = mustFindWallet(responseBody.getId());
        assertEquals(userId, wallet.getUserId());
    }

    @Test
    public void create_invalidInputData_nameNull() throws Exception {
        var userId = createUserOne();
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var requestBody = new WalletData();

        var request = post(WALLETS_URL).content(mapper.writeValueAsString(requestBody))
                                       .contentType(MediaType.APPLICATION_JSON_UTF8)
                                       .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        // verify wallet not inserted
        var wallets = walletRepository.findAll();
        assertEquals(0, wallets.size());
    }

    @Test
    public void create_invalidInputData_nameEmpty() throws Exception {
        var userId = createUserOne();
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var requestBody = new WalletData();
        requestBody.setName("");

        var request = post(WALLETS_URL).content(mapper.writeValueAsString(requestBody))
                                       .contentType(MediaType.APPLICATION_JSON_UTF8)
                                       .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        // verify wallet not inserted
        var wallets = walletRepository.findAll();
        assertEquals(0, wallets.size());
    }

    @Test
    public void getById_happyPath() throws Exception {
        var userId = createUserOne();
        var walletId = createWallet(userId, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var request = get(WALLETS_URL + "/" + walletId)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        var resString = mvc.perform(request)
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        var resData = mapper.readValue(resString, WalletData.class);

        assertEquals(NAME_1, resData.getName());
        assertEquals(walletId, resData.getId());
    }

    @Test
    public void getById_notFound() throws Exception {
        var userId = createUserOne();
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var request = get(WALLETS_URL + "/" + 1)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void getById_wrongOwner() throws Exception {
        var userId1 = createUserOne();
        var userId2 = createUserTwo();
        var walletId = createWallet(userId2, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId1);

        var request = get(WALLETS_URL + "/" + walletId)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void deleteById_mustHaveAuth() throws Exception {
        mvc.perform(delete(WALLETS_URL + "/" + 1))
           .andExpect(status().isUnauthorized())
           .andExpect(content().string(""));
    }

    @Test
    public void deleteById_happyPath() throws Exception {
        var userId = createUserOne();
        var walletId = createWallet(userId, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var request = delete(WALLETS_URL + "/" + walletId)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isOk())
           .andExpect(content().string(""));

        // verify that wallet was deleted
        var walletList = walletRepository.findAll();
        assertEquals(0, walletList.size());
    }

    @Test
    public void deleteById_notFound() throws Exception {
        var userId = createUserOne();
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var request = delete(WALLETS_URL + "/" + 1)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void deleteById_wrongOwner() throws Exception {
        var userId1 = createUserOne();
        var userId2 = createUserTwo();
        var walletId = createWallet(userId2, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId1);

        var request = get(WALLETS_URL + "/" + walletId)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));

        // verify that wallet was NOT deleted
        var walletList = walletRepository.findAll();
        assertEquals(1, walletList.size());
    }

    @Test
    public void updateById_mustHaveAuth() throws Exception {
        mvc.perform(post(WALLETS_URL + "/" + 1))
           .andExpect(status().isUnauthorized())
           .andExpect(content().string(""));
    }

    @Test
    public void updateById_happyPath() throws Exception {
        var userId = createUserOne();
        var walletId = createWallet(userId, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var requestBody = new WalletData();
        requestBody.setName(NAME_2);

        var request = post(WALLETS_URL + "/" + walletId)
                .content(mapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isOk())
           .andExpect(content().string(""));

        // verify persisted changes
        var wallet = mustFindWallet(walletId);
        assertEquals(NAME_2, wallet.getName());
    }

    @Test
    public void updateById_notFound() throws Exception {
        var userId = createUserOne();
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var requestBody = new WalletData();
        requestBody.setName(NAME_1);

        var request = post(WALLETS_URL + "/" + 1)
                .content(mapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void updateById_wrongOwner() throws Exception {
        var userId1 = createUserOne();
        var userId2 = createUserTwo();
        var walletId = createWallet(userId2, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId1);

        var requestBody = new WalletData();
        requestBody.setName(NAME_2);

        var request = post(WALLETS_URL + "/" + walletId)
                .content(mapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));

        // verify changes NOT persisted
        var wallet = mustFindWallet(walletId);
        assertEquals(NAME_1, wallet.getName());
    }

    @Test
    public void updateById_invalidData_nameNull() throws Exception {
        var userId = createUserOne();
        var walletId = createWallet(userId, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var requestBody = new WalletData();

        var request = post(WALLETS_URL + "/" + walletId)
                .content(mapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        // verify changes NOT persisted
        var wallet = mustFindWallet(walletId);
        assertEquals(NAME_1, wallet.getName());
    }

    @Test
    public void updateById_invalidData_nameEmpty() throws Exception {
        var userId = createUserOne();
        var walletId = createWallet(userId, NAME_1);
        var token = jwtTokenProvider.generateTokenFromId(userId);

        var requestBody = new WalletData();
        requestBody.setName("");

        var request = post(WALLETS_URL + "/" + walletId)
                .content(mapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);

        mvc.perform(request)
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        // verify changes NOT persisted
        var wallet = mustFindWallet(walletId);
        assertEquals(NAME_1, wallet.getName());
    }
}
