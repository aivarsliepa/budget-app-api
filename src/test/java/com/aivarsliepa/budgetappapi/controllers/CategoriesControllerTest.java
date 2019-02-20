package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.models.Category;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.repositories.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoriesController.class)
public class CategoriesControllerTest {

    private final String BASE_URL = "/categories";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    public void getCategoryList_works() throws Exception {
        var category = new Category();
        category.setType(CategoryType.EXPENSE);
        category.setParentId(1L);
        category.setId(2L);

        var categoryList = Collections.singletonList(category);

        given(categoryRepository.findAll()).willReturn(categoryList);

        mvc.perform(get(BASE_URL))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].type", is(equalTo(CategoryType.EXPENSE.toString()))))
           .andExpect(jsonPath("$[0].parentId", is(equalTo(1))))
           .andExpect(jsonPath("$[0].id", is(equalTo(2))));
    }

    @Test
    public void postCategory_fails_when() throws Exception {
//        mvc.perform(post(BASE_URL)).andExpect(status().is4xxClientError()).andExpect(content().string(""));
//        ModelMap
    }
}
