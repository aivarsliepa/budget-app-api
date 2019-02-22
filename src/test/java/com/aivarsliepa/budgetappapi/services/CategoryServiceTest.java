package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.mappers.CategoryMapper;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import com.aivarsliepa.budgetappapi.data.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
    private CategoryMapper categoryMapper;


    @Before
    public void setUp() {
        categoryService = new CategoryService(categoryRepository, categoryMapper);
    }

    @Test
    public void create_shouldSetIdToNull() {
        var data = new CategoryData();
        data.setId(1L);

        categoryService.create(data);

        var argument = ArgumentCaptor.forClass(CategoryData.class);

        verify(categoryMapper).map(argument.capture());
        assertNull(argument.getValue().getId());
    }

    @Test
    public void updateById_shouldSetIdToGivenId() {
        var id = Long.valueOf(2);
        var data = new CategoryData();
        data.setId(1L);

        given(categoryRepository.existsById(id)).willReturn(true);
        given(categoryMapper.map(nullable(CategoryModel.class))).willReturn(new CategoryData());

        categoryService.updateById(id, data);

        var argument = ArgumentCaptor.forClass(CategoryData.class);

        verify(categoryMapper).map(argument.capture());
        assertEquals(argument.getValue().getId(), id);
    }

    @Test
    public void updateById_shouldReturnOptionalWithPresentData_whenFoundAndUpdated() {
        var id = Long.valueOf(2);
        var inputData = mock(CategoryData.class);
        var expected = mock(CategoryData.class);

        given(categoryRepository.existsById(id)).willReturn(true);
        given(categoryMapper.map(nullable(CategoryModel.class))).willReturn(expected);

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

        given(categoryRepository.existsById(id)).willReturn(false);

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
        verify(categoryRepository, times(1)).deleteById(id);
    }

    @Test
    public void findAll_test() {
        var data1 = mock(CategoryData.class);
        var data2 = mock(CategoryData.class);

        var model1 = mock(CategoryModel.class);
        var model2 = mock(CategoryModel.class);

        given(categoryRepository.findAll()).willReturn(Arrays.asList(model1, model2));
        given(categoryMapper.map(model1)).willReturn(data1);
        given(categoryMapper.map(model2)).willReturn(data2);

        var result = categoryService.findAll();
        assertThat(result, containsInAnyOrder(data1, data2));
    }

    @Test
    public void findById_shouldReturnOptionalWithPresentData_whenFound() {
        var id = 1L;
        var data = mock(CategoryData.class);
        var model = mock(CategoryModel.class);

        given(categoryRepository.findById(id)).willReturn(Optional.of(model));
        given(categoryMapper.map(model)).willReturn(data);

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
}
