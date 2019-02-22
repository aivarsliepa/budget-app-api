package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.services.CategoryService;
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
@WebMvcTest(CategoriesController.class)
public class CategoriesControllerTest {

    private final String BASE_URL = "/categories";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void getList_works() throws Exception {
        var data = new CategoryData();
        data.setCategoryType(CategoryType.EXPENSE);
        data.setParentId(1L);
        data.setId(2L);

        given(categoryService.findAll()).willReturn(Collections.singletonList(data));

        mvc.perform(get(BASE_URL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].type", is(equalTo(CategoryType.EXPENSE.toString()))))
           .andExpect(jsonPath("$[0].parentId", is(equalTo(1))))
           .andExpect(jsonPath("$[0].id", is(equalTo(2))));
    }

    @Test
    public void create_fails_whenBodyIsEmpty() throws Exception {
        mvc.perform(post(BASE_URL))
           .andExpect(status().is4xxClientError())
           .andExpect(content().string(""));

        verifyZeroInteractions(categoryService);
    }

    @Test
    public void create_fails_whenBodyIsInvalid() throws Exception {
        var data = mock(CategoryData.class);

        mvc.perform(post(BASE_URL, data))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(categoryService);
    }

    @Test
    public void create_returnsData_whenBodyValid() throws Exception {
        var requestData = new CategoryData();
        requestData.setType("INCOME");

        var responseData = new CategoryData();
        responseData.setType("EXPENSE");
        responseData.setId(1L);

        given(categoryService.create(requestData)).willReturn(responseData);

        var resString = mvc.perform(post(BASE_URL)
                                            .content(mapper.writeValueAsString(requestData))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andExpect(jsonPath("$.type", is(equalTo(CategoryType.EXPENSE.toString()))))
                           .andExpect(jsonPath("$.id", is(equalTo(1))))
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, CategoryData.class), responseData);
    }

    @Test
    public void getById_returnsNotFoundStatusAndEmptyBody_whenNoMatchingDataFound() throws Exception {
        var id = 1L;

        given(categoryService.findById(id)).willReturn(Optional.empty());

        mvc.perform(get(BASE_URL + "/" + id))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void getById_returnsFoundData_whenIdMatches() throws Exception {
        var id = 1L;

        var data = new CategoryData();
        data.setType("EXPENSE");
        data.setId(1L);

        given(categoryService.findById(id)).willReturn(Optional.of(data));

        var resString = mvc.perform(get(BASE_URL + "/" + id))
                           .andExpect(status().isOk())
                           .andExpect(jsonPath("$.type", is(equalTo(CategoryType.EXPENSE.toString()))))
                           .andExpect(jsonPath("$.id", is(equalTo(1))))
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, CategoryData.class), data);
    }

    @Test
    public void updateById_returnsStatusNotFound_whenNotFound() throws Exception {
        var data = new CategoryData();
        data.setType("EXPENSE");
        var id = 1L;

        given(categoryService.updateById(anyLong(), any(CategoryData.class))).willReturn(Optional.empty());

        mvc.perform(post(BASE_URL + "/" + id)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void updateById_returnsStatusBadRequest_whenInvalidBody() throws Exception {
        var data = new CategoryData();
        var id = 1L;

        mvc.perform(post(BASE_URL + "/" + id)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));
    }

    @Test
    public void updateById_returnsUpdatedData_whenValidRequest() throws Exception {
        var data = new CategoryData();
        data.setType("EXPENSE");
        var id = 1L;

        given(categoryService.updateById(anyLong(), any(CategoryData.class))).willReturn(Optional.of(data));

        var resString = mvc.perform(post(BASE_URL + "/" + id)
                                            .content(mapper.writeValueAsString(data))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andExpect(jsonPath("$.type", is(equalTo(CategoryType.EXPENSE.toString()))))
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, CategoryData.class), data);
    }

    @Test
    public void deleteById_returnsStatusNotFound_whenNotFound() throws Exception {
        var id = 1L;

        given(categoryService.deleteById(id)).willReturn(false);

        mvc.perform(delete(BASE_URL + "/" + id))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void deleteById_returnsStatusOk_whenFoundAndDeleted() throws Exception {
        var id = 1L;

        given(categoryService.deleteById(id)).willReturn(true);

        mvc.perform(delete(BASE_URL + "/" + id))
           .andExpect(status().isOk())
           .andExpect(content().string(""));
    }
}
