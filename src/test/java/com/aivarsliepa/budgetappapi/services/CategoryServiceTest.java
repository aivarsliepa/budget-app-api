package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.category.CategoryData;
import com.aivarsliepa.budgetappapi.data.category.CategoryModel;
import com.aivarsliepa.budgetappapi.data.category.CategoryPopulator;
import com.aivarsliepa.budgetappapi.data.category.CategoryRepository;
import com.aivarsliepa.budgetappapi.data.user.UserModel;
import com.aivarsliepa.budgetappapi.exceptions.ResourceNotFoundException;
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
    private static final Long CATEGORY_ID = 1L;
    private static final Long USER_ID = 2L;

    private UserModel userMock = mock(UserModel.class);

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

        given(authService.getCurrentUserId()).willReturn(USER_ID);
        given(authService.getCurrentUser()).willReturn(userMock);
    }

    @Test
    public void updateById_shouldUpdate_whenFound() {
        var inputData = mock(CategoryData.class);
        var model = mock(CategoryModel.class);

        given(categoryRepository.findByIdAndUserId(CATEGORY_ID, USER_ID)).willReturn(Optional.of(model));

        categoryService.updateById(CATEGORY_ID, inputData);

        verify(categoryPopulator).populateModel(model, inputData);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateById_shouldThrow_whenNotFound() {
        var data = mock(CategoryData.class);

        given(categoryRepository.findByIdAndUserId(CATEGORY_ID, USER_ID)).willReturn(Optional.empty());

        categoryService.updateById(CATEGORY_ID, data);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteById_shouldNotDelete_andShouldThrow_whenRecordDoesNotExist() {
        given(categoryRepository.existsByIdAndUserId(CATEGORY_ID, USER_ID)).willReturn(false);

        categoryService.deleteById(CATEGORY_ID);
    }

    @Test
    public void deleteById_shouldDelete_whenRecordDoesExist() {
        given(categoryRepository.existsByIdAndUserId(CATEGORY_ID, USER_ID)).willReturn(true);

        categoryService.deleteById(CATEGORY_ID);

        verify(categoryRepository).deleteByIdAndUserId(CATEGORY_ID, USER_ID);
    }

    @Test
    public void getListForCurrentUser_test() {
        var data1 = mock(CategoryData.class);
        var data2 = mock(CategoryData.class);

        var model1 = mock(CategoryModel.class);
        var model2 = mock(CategoryModel.class);

        given(categoryRepository.findAllByUserId(USER_ID)).willReturn(Arrays.asList(model1, model2));
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model1))).willReturn(data1);
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model2))).willReturn(data2);

        var result = categoryService.getListForCurrentUser();
        assertThat(result, containsInAnyOrder(data1, data2));
    }

    @Test
    public void findById_shouldReturnOptionalWithPresentData_whenFound() {
        var data = mock(CategoryData.class);
        var model = mock(CategoryModel.class);

        given(categoryRepository.findByIdAndUserId(CATEGORY_ID, USER_ID)).willReturn(Optional.of(model));
        given(categoryPopulator.populateData(any(CategoryData.class), eq(model))).willReturn(data);

        var result = categoryService.findById(CATEGORY_ID);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), data);
    }

    @Test
    public void findById_shouldReturnEmptyOptional_whenNotFound() {
        given(categoryRepository.findByIdAndUserId(CATEGORY_ID, USER_ID)).willReturn(Optional.empty());

        var result = categoryService.findById(CATEGORY_ID);

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
    public void create_shouldSetCurrentUser() {
        var data = mock(CategoryData.class);
        var model = new CategoryModel();

        var argumentCaptor = ArgumentCaptor.forClass(CategoryModel.class);

        given(categoryPopulator.populateModel(any(CategoryModel.class), eq(data))).willReturn(model);

        categoryService.create(data);

        verify(categoryRepository).save(argumentCaptor.capture());
        assertEquals(userMock, argumentCaptor.getValue().getUser());
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
