package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import com.aivarsliepa.budgetappapi.data.wallet.WalletPopulator;
import com.aivarsliepa.budgetappapi.data.wallet.WalletRepository;
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
public class WalletServiceTest {

    private static final Long WALLET_ID = 1L;
    private static final Long USER_ID = 2L;

    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private WalletPopulator walletPopulator;

    @MockBean
    private AuthService authService;


    @Before
    public void setUp() {
        walletService = new WalletService(walletRepository, walletPopulator, authService);

        given(authService.getCurrentUserId()).willReturn(USER_ID);
    }

    @Test
    public void getList_test() {
        var data1 = mock(WalletData.class);
        var data2 = mock(WalletData.class);

        var model1 = mock(WalletModel.class);
        var model2 = mock(WalletModel.class);

        given(walletRepository.findAllByUserId(USER_ID)).willReturn(Arrays.asList(model1, model2));
        given(walletPopulator.populateData(any(WalletData.class), eq(model1))).willReturn(data1);
        given(walletPopulator.populateData(any(WalletData.class), eq(model2))).willReturn(data2);

        var result = walletService.getListForCurrentUser();
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
    public void create_shouldSetCurrentUserId() {
        var data = mock(WalletData.class);
        var model = new WalletModel();

        given(walletPopulator.populateModel(any(WalletModel.class), eq(data))).willReturn(model);

        walletService.create(data);

        var argumentCaptor = ArgumentCaptor.forClass(WalletModel.class);

        verify(walletRepository).save(argumentCaptor.capture());
        assertEquals(USER_ID, argumentCaptor.getValue().getUserId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteById_shouldNotDelete_andShouldThrow_whenRecordDoesNotExist() {
        given(walletRepository.existsByIdAndUserId(WALLET_ID, USER_ID)).willReturn(false);

        walletService.deleteById(WALLET_ID);

        verify(walletRepository, never()).deleteByIdAndUserId(any(), any());
    }

    @Test
    public void deleteById_shouldDelete_whenRecordDoesExist() {
        given(walletRepository.existsByIdAndUserId(WALLET_ID, USER_ID)).willReturn(true);

        walletService.deleteById(WALLET_ID);

        verify(walletRepository).deleteByIdAndUserId(WALLET_ID, USER_ID);
    }

    @Test
    public void findById_shouldReturnOptionalWithPresentData_whenFound() {
        var data = mock(WalletData.class);
        var model = mock(WalletModel.class);

        given(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID)).willReturn(Optional.of(model));
        given(walletPopulator.populateData(any(WalletData.class), eq(model))).willReturn(data);

        var result = walletService.findById(WALLET_ID);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), data);
    }

    @Test
    public void findById_shouldReturnEmptyOptional_whenNotFound() {
        given(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID)).willReturn(Optional.empty());

        var result = walletService.findById(WALLET_ID);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }


    @Test
    public void updateById_shouldUpdate_whenFound() {
        var inputData = mock(WalletData.class);
        var model = mock(WalletModel.class);

        given(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID)).willReturn(Optional.of(model));

        walletService.updateById(WALLET_ID, inputData);

        verify(walletPopulator).populateModel(model, inputData);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateById_shouldThrow_whenNotFound() {
        var inputData = mock(WalletData.class);

        given(walletRepository.findByIdAndUserId(WALLET_ID, USER_ID)).willReturn(Optional.empty());

        walletService.updateById(WALLET_ID, inputData);
    }
}
