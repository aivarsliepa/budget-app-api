package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.wallet.WalletRepository;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryPopulator;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryRepository;
import com.aivarsliepa.budgetappapi.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class WalletEntryServiceTest {

    private static final Long WALLET_ID = 3L;
    private static final Long ENTRY_ID = 1L;
    private static final Long USER_ID = 2L;

    private WalletEntryService walletEntryService;

    @MockBean
    private WalletEntryRepository walletEntryRepository;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private WalletEntryPopulator walletEntryPopulator;

    @MockBean
    private AuthService authService;

    @MockBean
    private WalletService walletService;

    @Before
    public void setUp() {
        walletEntryService = new WalletEntryService(walletEntryRepository, walletEntryPopulator, walletRepository,
                                                    authService, walletService);

        given(authService.getCurrentUserId()).willReturn(USER_ID);
    }

    @Test
    public void getListByWalletId_test() {
        var data1 = mock(WalletEntryData.class);
        var data2 = mock(WalletEntryData.class);

        var model1 = mock(WalletEntryModel.class);
        var model2 = mock(WalletEntryModel.class);

        given(walletEntryRepository.findAllByUserIdAndWalletId(USER_ID, WALLET_ID))
                .willReturn(Arrays.asList(model1, model2));
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model1))).willReturn(data1);
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model2))).willReturn(data2);

        var result = walletEntryService.getListByWalletId(WALLET_ID);

        assertThat(result, containsInAnyOrder(data1, data2));
    }

    @Test
    public void findByIdAndWalletId_shouldReturnData_whenFound() {
        var data = mock(WalletEntryData.class);
        var model = mock(WalletEntryModel.class);

        given(walletEntryRepository.findByIdAndUserIdAndWalletId(ENTRY_ID, USER_ID, WALLET_ID))
                .willReturn(Optional.of(model));
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model))).willReturn(data);

        var actual = walletEntryService.findByIdAndWalletId(ENTRY_ID, WALLET_ID);

        assertEquals(data, actual);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByIdAndWalletId_shouldThrow_whenNotFound() {
        given(walletEntryRepository.findByIdAndUserIdAndWalletId(ENTRY_ID, USER_ID, WALLET_ID))
                .willReturn(Optional.empty());

        walletEntryService.findByIdAndWalletId(ENTRY_ID, WALLET_ID);
    }

//    @Test
//    public void createEntryToWallet_shouldPersist_whenWalletExists() {
//        var data = mock(WalletEntryData.class);
//        var model = mock(WalletEntryModel.class);
//
//        given(walletRepository.existsById(WALLET_ID)).willReturn(true);
//        given(walletEntryPopulator.populateModel(any(WalletEntryModel.class), eq(data))).willReturn(model);
//        given(walletEntryPopulator.populateData(any(WalletEntryData.class), nullable(WalletEntryModel.class)))
//                .willReturn(data);
//
//        walletEntryService.createEntryToWallet(WALLET_ID, data);
//
//        verify(walletEntryRepository).save(model);
//    }
//
//    @Test
//    public void createEntryToWallet_shouldReturnData_whenWalletExists() {
//        var inputData = mock(WalletEntryData.class);
//        var expected = mock(WalletEntryData.class);
//        var model1 = mock(WalletEntryModel.class);
//        var model2 = mock(WalletEntryModel.class);
//
//        given(walletRepository.existsById(WALLET_ID)).willReturn(true);
//        given(walletEntryPopulator.populateModel(any(WalletEntryModel.class), eq(inputData))).willReturn(model1);
//        given(walletEntryRepository.save(model1)).willReturn(model2);
//        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model2))).willReturn(expected);
//
//        var result = walletEntryService.createEntryToWallet(WALLET_ID, inputData);
//
//        assertEquals(expected, result);
//    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteByIdAndWalletId_shouldThrow_whenNotFound() {
        given(walletEntryRepository.existsByIdAndUserIdAndWalletId(ENTRY_ID, USER_ID, WALLET_ID)).willReturn(false);

        walletEntryService.deleteByIdAndWalletId(ENTRY_ID, WALLET_ID);
    }

    @Test
    public void deleteByIdAndWalletId_shouldDelete_andShouldReturnTrue_whenRecordDoesExist() {
        given(walletEntryRepository.existsByIdAndUserIdAndWalletId(ENTRY_ID, USER_ID, WALLET_ID)).willReturn(true);

        walletEntryService.deleteByIdAndWalletId(ENTRY_ID, WALLET_ID);

        verify(walletEntryRepository).deleteByIdAndUserIdAndWalletId(ENTRY_ID, USER_ID, WALLET_ID);
    }

    @Test
    public void updateById_shouldPopulateModel_whenFound() {
        var inputData = mock(WalletEntryData.class);
        var model = mock(WalletEntryModel.class);

        given(walletEntryRepository.findByIdAndUserIdAndWalletId(ENTRY_ID, USER_ID, WALLET_ID))
                .willReturn(Optional.of(model));

        walletEntryService.updateByIdAndWalletId(ENTRY_ID, WALLET_ID, inputData);

        verify(walletEntryPopulator).populateModel(model, inputData);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateById_shouldReturnEmptyOptional_whenNotFound() {
        var inputData = mock(WalletEntryData.class);

        given(walletEntryRepository.findByIdAndUserIdAndWalletId(ENTRY_ID, USER_ID, WALLET_ID))
                .willReturn(Optional.empty());

        walletEntryService.updateByIdAndWalletId(ENTRY_ID, WALLET_ID, inputData);
    }
}
