package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.category.CategoryData;
import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoriesController.class)
public class CategoriesControllerTest {

    private static final Long CATEGORY_ID = 1L;
    private static final Long PARENT_CATEGORY_ID = 2L;

    private static final String BASE_URL = URLPaths.Categories.BASE;
    private static final String CATEGORY_URL = BASE_URL + "/" + CATEGORY_ID;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CategoryService categoryService;

    private CategoryData createValidData() {
        var data = new CategoryData();
        data.setType(CategoryType.EXPENSE);
        data.setParentId(PARENT_CATEGORY_ID);
        data.setId(CATEGORY_ID);
        return data;
    }

    @Test
    public void getList_works() throws Exception {
        var data = createValidData();
        var expectedResponseData = new CategoryData[]{data};

        given(categoryService.findAll()).willReturn(Collections.singletonList(data));

        var resString = mvc.perform(get(BASE_URL))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertArrayEquals(mapper.readValue(resString, CategoryData[].class), expectedResponseData);
    }

    @Test
    public void create_fails_whenBodyIsEmpty() throws Exception {
        mvc.perform(post(BASE_URL))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(categoryService);
    }

    @Test
    public void create_fails_whenBodyIsInvalid() throws Exception {
        var data = new CategoryData();

        mvc.perform(post(BASE_URL)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(categoryService);
    }

    @Test
    public void create_returnsData_whenBodyValid() throws Exception {
        var requestData = createValidData();
        var responseData = createValidData();
        var expected = createValidData();

        given(categoryService.create(requestData)).willReturn(responseData);

        var resString = mvc.perform(post(BASE_URL)
                                            .content(mapper.writeValueAsString(requestData))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, CategoryData.class), expected);
    }

    @Test
    public void getById_returnsNotFoundStatusAndEmptyBody_whenNoMatchingDataFound() throws Exception {
        given(categoryService.findById(CATEGORY_ID)).willReturn(Optional.empty());

        mvc.perform(get(CATEGORY_URL))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void getById_returnsFoundData_whenIdMatches() throws Exception {
        var data = createValidData();
        var expected = createValidData();

        given(categoryService.findById(CATEGORY_ID)).willReturn(Optional.of(data));

        var resString = mvc.perform(get(CATEGORY_URL))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, CategoryData.class), expected);
    }

    @Test
    public void updateById_returnsStatusNotFound_whenNotFound() throws Exception {
        var data = createValidData();

        given(categoryService.updateById(anyLong(), any(CategoryData.class))).willReturn(Optional.empty());

        mvc.perform(post(CATEGORY_URL)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void updateById_returnsStatusBadRequest_whenInvalidBody() throws Exception {
        var data = new CategoryData();

        mvc.perform(post(CATEGORY_URL)
                            .content(mapper.writeValueAsString(data))
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(content().string(""));

        verifyZeroInteractions(categoryService);
    }

    @Test
    public void updateById_returnsUpdatedData_whenValidRequest() throws Exception {
        var data = createValidData();
        var expected = createValidData();

        given(categoryService.updateById(anyLong(), any(CategoryData.class))).willReturn(Optional.of(data));

        var resString = mvc.perform(post(CATEGORY_URL)
                                            .content(mapper.writeValueAsString(data))
                                            .contentType(MediaType.APPLICATION_JSON_UTF8))
                           .andExpect(status().isOk())
                           .andReturn()
                           .getResponse()
                           .getContentAsString();

        assertEquals(mapper.readValue(resString, CategoryData.class), expected);
    }

    @Test
    public void deleteById_returnsStatusNotFound_whenNotFound() throws Exception {
        given(categoryService.deleteById(CATEGORY_ID)).willReturn(false);

        mvc.perform(delete(CATEGORY_URL))
           .andExpect(status().isNotFound())
           .andExpect(content().string(""));
    }

    @Test
    public void deleteById_returnsStatusOk_whenFoundAndDeleted() throws Exception {
        given(categoryService.deleteById(CATEGORY_ID)).willReturn(true);

        mvc.perform(delete(CATEGORY_URL))
           .andExpect(status().isOk())
           .andExpect(content().string(""));

        verify(categoryService).deleteById(CATEGORY_ID);
    }
}
