package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.dto.WalletData;
import com.aivarsliepa.budgetappapi.data.models.WalletModel;
import com.aivarsliepa.budgetappapi.data.populators.WalletPopulator;
import com.aivarsliepa.budgetappapi.data.repositories.WalletRepository;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
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
public class WalletServiceTest {

    private static final Long ID = 1L;

    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private WalletPopulator walletPopulator;


    @Before
    public void setUp() {
        walletService = new WalletService(walletRepository, walletPopulator);
    }

    @Test
    public void getList_test() {
        var data1 = mock(WalletData.class);
        var data2 = mock(WalletData.class);

        var model1 = mock(WalletModel.class);
        var model2 = mock(WalletModel.class);

        given(walletRepository.findAll()).willReturn(Arrays.asList(model1, model2));
        given(walletPopulator.populateData(any(WalletData.class), eq(model1))).willReturn(data1);
        given(walletPopulator.populateData(any(WalletData.class), eq(model2))).willReturn(data2);

        var result = walletService.getList();
        assertThat(result, containsInAnyOrder(data1, data2));
    }

    @Test
    public void create_shouldPersist() {
        var data = mock(WalletData.class);
        var model1 = mock(WalletModel.class);

        given(walletPopulator.populateModel(any(WalletModel.class), eq(data))).willReturn(model1);

        walletService.create(data);

        verify(walletRepository).save(model1);
    }

    @Test
    public void create_shouldReturnData() {
        var inputData = mock(WalletData.class);
        var expected = mock(WalletData.class);
        var model1 = mock(WalletModel.class);
        var model2 = mock(WalletModel.class);

        given(walletPopulator.populateModel(any(WalletModel.class), eq(inputData))).willReturn(model1);
        given(walletRepository.save(model1)).willReturn(model2);
        given(walletPopulator.populateData(any(WalletData.class), eq(model2))).willReturn(expected);

        var actual = walletService.create(inputData);

        assertEquals(actual, expected);
    }

    @Test
    public void deleteById_shouldNotDelete_andShouldReturnFalse_whenRecordDoesNotExist() {
        given(walletRepository.existsById(ID)).willReturn(false);

        var result = walletService.deleteById(ID);

        assertFalse(result);
        verify(walletRepository, never()).deleteById(any());
    }

    @Test
    public void deleteById_shouldDelete_andShouldReturnTrue_whenRecordDoesExist() {
        given(walletRepository.existsById(ID)).willReturn(true);

        var result = walletService.deleteById(ID);

        assertTrue(result);
        verify(walletRepository).deleteById(ID);
    }

    @Test
    public void findById_shouldReturnOptionalWithPresentData_whenFound() {
        var data = mock(WalletData.class);
        var model = mock(WalletModel.class);

        given(walletRepository.findById(ID)).willReturn(Optional.of(model));
        given(walletPopulator.populateData(any(WalletData.class), eq(model))).willReturn(data);

        var result = walletService.findById(ID);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), data);
    }

    @Test
    public void findById_shouldReturnEmptyOptional_whenNotFound() {
        given(walletRepository.findById(any())).willReturn(Optional.empty());

        var result = walletService.findById(1L);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }


    @Test
    public void updateById_shouldReturnOptionalWithPresentData_whenFoundAndUpdated() {
        var inputData = mock(WalletData.class);
        var expected = mock(WalletData.class);
        var model = mock(WalletModel.class);

        given(walletRepository.findById(ID)).willReturn(Optional.of(model));
        given(walletPopulator.populateData(any(WalletData.class), nullable(WalletModel.class)))
                .willReturn(expected);

        var result = walletService.updateById(ID, inputData);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), expected);
    }

    @Test
    public void updateById_shouldReturnEmptyOptional_whenNotFound() {
        var inputData = mock(WalletData.class);

        given(walletRepository.findById(ID)).willReturn(Optional.empty());

        var result = walletService.updateById(ID, inputData);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }
}
