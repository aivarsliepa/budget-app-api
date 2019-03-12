package com.aivarsliepa.budgetappapi.integrationtests;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.payloads.JwtAuthResponseBody;
import com.aivarsliepa.budgetappapi.data.payloads.LoginRequestBody;
import com.aivarsliepa.budgetappapi.data.payloads.RegisterRequestBody;
import com.aivarsliepa.budgetappapi.data.user.UserModel;
import com.aivarsliepa.budgetappapi.data.user.UserRepository;
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

import static io.jsonwebtoken.lang.Assert.hasText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AuthTest {
    private static final String REGISTER_URL = URLPaths.Auth.BASE + URLPaths.Auth.REGISTER;
    private static final String LOGIN_URL = URLPaths.Auth.BASE + URLPaths.Auth.LOGIN;

    private static final String USERNAME_1 = "username_1";
    private static final String PASSWORD_1 = "password_1";
    private static final String PASSWORD_2 = "password_2";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        userRepository.deleteAll();
    }

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    public void register_happyPath() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setPassword(PASSWORD_1);
        requestBody.setUsername(USERNAME_1);
        requestBody.setConfirmPassword(PASSWORD_1);

        var response = mvc.perform(post(REGISTER_URL)
                                           .content(mapper.writeValueAsString(requestBody))
                                           .contentType(MediaType.APPLICATION_JSON_UTF8))
                          .andExpect(status().isOk())
                          .andReturn()
                          .getResponse()
                          .getContentAsString();

        // validate jwt is in response
        var responseBody = mapper.readValue(response, JwtAuthResponseBody.class);
        hasText(responseBody.getToken());


        // validate that registered user is persisted
        var persistedUserOpt = userRepository.findByUsername(USERNAME_1);
        if (persistedUserOpt.isEmpty()) {
            fail("User not persisted");
        }
        assertEquals(persistedUserOpt.get().getUsername(), USERNAME_1);
    }


    @Test
    public void register_invalidInputData_usernameNull() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setPassword(PASSWORD_1);
        requestBody.setConfirmPassword(PASSWORD_1);

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void register_invalidInputData_passwordNull() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setUsername(USERNAME_1);
        requestBody.setConfirmPassword(PASSWORD_1);

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void register_invalidInputData_confirmPasswordNull() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setPassword(PASSWORD_1);
        requestBody.setUsername(USERNAME_1);

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void register_invalidInputData_usernameEmpty() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setUsername("");
        requestBody.setPassword(PASSWORD_1);
        requestBody.setConfirmPassword(PASSWORD_1);

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void register_invalidInputData_passwordEmpty() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setUsername(USERNAME_1);
        requestBody.setPassword("");
        requestBody.setConfirmPassword(PASSWORD_1);

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void register_invalidInputData_confirmPasswordEmpty() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setPassword(PASSWORD_1);
        requestBody.setUsername(USERNAME_1);
        requestBody.setConfirmPassword("");

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void register_invalidInputData_passwordsDoesNotMatch() throws Exception {
        var requestBody = new RegisterRequestBody();
        requestBody.setPassword(PASSWORD_1);
        requestBody.setConfirmPassword(PASSWORD_2);

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void register_usernameAlreadyExists() throws Exception {
        // make sure user exists
        var existingUser = new UserModel();
        var encodedPassword = passwordEncoder.encode(PASSWORD_1);
        existingUser.setPassword(encodedPassword);
        existingUser.setUsername(USERNAME_1);
        userRepository.save(existingUser);

        var requestBody = new RegisterRequestBody();
        requestBody.setPassword(PASSWORD_2);
        requestBody.setUsername(USERNAME_1);
        requestBody.setConfirmPassword(PASSWORD_2);

        mvc.perform(post(REGISTER_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        // verify that user count is still 1
        var userList = userRepository.findAll();
        assertEquals(1, userList.size());

        // verify that user password was not overwritten
        var user = userRepository.findByUsername(USERNAME_1);
        if (user.isEmpty()) {
            fail("User should be not be empty!");
        }
        assertEquals(encodedPassword, user.get().getPassword());
    }

    @Test
    public void login_happyPath() throws Exception {
        // make sure user exists
        var user = new UserModel();
        user.setPassword(passwordEncoder.encode(PASSWORD_1));
        user.setUsername(USERNAME_1);
        userRepository.save(user);

        var requestBody = new LoginRequestBody();
        requestBody.setPassword(PASSWORD_1);
        requestBody.setUsername(USERNAME_1);

        var response = mvc.perform(post(LOGIN_URL)
                                           .content(mapper.writeValueAsString(requestBody))
                                           .contentType(MediaType.APPLICATION_JSON_UTF8))
                          .andExpect(status().isOk())
                          .andReturn()
                          .getResponse()
                          .getContentAsString();

        // validate jwt is in response
        var responseBody = mapper.readValue(response, JwtAuthResponseBody.class);
        hasText(responseBody.getToken());
    }

    @Test
    public void login_invalidPassword() throws Exception {
        // make sure user exists
        var user = new UserModel();
        user.setPassword(passwordEncoder.encode(PASSWORD_1));
        user.setUsername(USERNAME_1);
        userRepository.save(user);

        var requestBody = new LoginRequestBody();
        requestBody.setPassword(PASSWORD_2);
        requestBody.setUsername(USERNAME_1);

        mvc.perform(post(LOGIN_URL)
                            .content(mapper.writeValueAsString(requestBody))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isUnauthorized())
           .andExpect(content().string(""));
    }
}
