package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.category.CategoryData;
import com.aivarsliepa.budgetappapi.data.category.CategoryModel;
import com.aivarsliepa.budgetappapi.data.category.CategoryPopulator;
import com.aivarsliepa.budgetappapi.data.category.CategoryRepository;
import com.aivarsliepa.budgetappapi.security.RestAuthenticationEntryPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CategoryServiceTest {
    private static final Long ID = 1L;

    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryPopulator categoryPopulator;

    @MockBean
    private AuthService authService;

    @Before
    public void setUp() {
        categoryService = new CategoryService(categoryRepository, categoryPopulator, authService);
    }

    @Test
    public void updateById_shouldReturnOptionalWithPresentData_whenFoundAndUpdated() {
        var inputData = mock(CategoryData.class);
        var expected = mock(CategoryData.class);

        given(categoryRepository.findById(ID)).willReturn(Optional.of(mock(CategoryModel.class)));
        given(categoryPopulator.populateData(any(CategoryData.class), nullable(CategoryModel.class)))
                .willReturn(expected);

        var result = categoryService.updateById(ID, inputData);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), expected);
    }

    @Test
    public void updateById_shouldReturnEmptyOptional_whenNotFound() {
        var data = mock(CategoryData.class);

        given(categoryRepository.findById(ID)).willReturn(Optional.empty());

        var result = categoryService.updateById(ID, data);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }

    @Test
    public void deleteById_shouldNotDelete_andShouldReturnFalse_whenRecordDoesNotExist() {
        given(categoryRepository.existsById(ID)).willReturn(false);

        var result = categoryService.deleteById(ID);

        assertFalse(result);
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    public void deleteById_shouldDelete_andShouldReturnTrue_whenRecordDoesExist() {
        given(categoryRepository.existsById(ID)).willReturn(true);

        var result = categoryService.deleteById(ID);

        assertTrue(result);
        verify(categoryRepository).deleteById(ID);
    }

    @Test
    public void findAll_test() {
        var data1 = mock(CategoryData.class);
        var data2 = mock(CategoryData.class);

        var model1 = mock(CategoryModel.class);
        var model2 = mock(CategoryModel.class);

        given(categoryRepository.findAll()).willReturn(Arrays.asList(model1, model2));
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model1))).willReturn(data1);
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model2))).willReturn(data2);

        var result = categoryService.findAll();
        assertThat(result, containsInAnyOrder(data1, data2));
    }

    @Test
    public void findById_shouldReturnOptionalWithPresentData_whenFound() {
        var data = mock(CategoryData.class);
        var model = mock(CategoryModel.class);

        given(categoryRepository.findById(ID)).willReturn(Optional.of(model));
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model))).willReturn(data);

        var result = categoryService.findById(ID);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), data);
    }

    @Test
    public void findById_shouldReturnEmptyOptional_whenNotFound() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        var result = categoryService.findById(ID);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }

    @Test
    public void create_shouldPersist() {
        var data = mock(CategoryData.class);
        var model = mock(CategoryModel.class);

        given(categoryPopulator.populateModel(any(CategoryModel.class), eq(data))).willReturn(model);

        categoryService.create(data);

        verify(categoryRepository).save(model);
    }

    @Test
    public void create_shouldReturnData() {
        var inputData = mock(CategoryData.class);
        var expected = mock(CategoryData.class);
        var model1 = mock(CategoryModel.class);
        var model2 = mock(CategoryModel.class);

        given(categoryPopulator.populateModel(any(CategoryModel.class), eq(inputData))).willReturn(model1);
        given(categoryRepository.save(model1)).willReturn(model2);
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model2))).willReturn(expected);

        var actual = categoryService.create(inputData);

        assertEquals(actual, expected);
    }
}
