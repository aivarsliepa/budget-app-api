package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import com.aivarsliepa.budgetappapi.data.populators.CategoryPopulator;
import com.aivarsliepa.budgetappapi.data.repositories.CategoryRepository;
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

    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryPopulator categoryPopulator;


    @Before
    public void setUp() {
        categoryService = new CategoryService(categoryRepository, categoryPopulator);
    }

    @Test
    public void updateById_shouldReturnOptionalWithPresentData_whenFoundAndUpdated() {
        var id = Long.valueOf(2);
        var inputData = mock(CategoryData.class);
        var expected = mock(CategoryData.class);

        given(categoryRepository.findById(id)).willReturn(Optional.of(mock(CategoryModel.class)));
        given(categoryPopulator.populateData(any(CategoryData.class), nullable(CategoryModel.class)))
                .willReturn(expected);

        var result = categoryService.updateById(id, inputData);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), expected);
    }

    @Test
    public void updateById_shouldReturnEmptyOptional_whenNotFound() {
        var id = Long.valueOf(2);
        var inputData = mock(CategoryData.class);

        given(categoryRepository.findById(id)).willReturn(Optional.empty());

        var result = categoryService.updateById(id, inputData);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }

    @Test
    public void deleteById_shouldNotDelete_andShouldReturnFalse_whenRecordDoesNotExist() {
        var id = Long.valueOf(2);

        given(categoryRepository.existsById(id)).willReturn(false);

        var result = categoryService.deleteById(id);

        assertFalse(result);
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    public void deleteById_shouldDelete_andShouldReturnTrue_whenRecordDoesExist() {
        var id = Long.valueOf(2);

        given(categoryRepository.existsById(id)).willReturn(true);

        var result = categoryService.deleteById(id);

        assertTrue(result);
        verify(categoryRepository).deleteById(id);
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
        var id = 1L;
        var data = mock(CategoryData.class);
        var model = mock(CategoryModel.class);

        given(categoryRepository.findById(id)).willReturn(Optional.of(model));
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model))).willReturn(data);

        var result = categoryService.findById(id);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), data);
    }

    @Test
    public void findById_shouldReturnEmptyOptional_whenNotFound() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());

        var result = categoryService.findById(1L);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }

    @Test
    public void create_shouldPersist() {
        var data = mock(CategoryData.class);
        var model1 = mock(CategoryModel.class);

        given(categoryPopulator.populateModel(any(CategoryModel.class), eq(data))).willReturn(model1);

        categoryService.create(data);

        verify(categoryRepository).save(model1);
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
