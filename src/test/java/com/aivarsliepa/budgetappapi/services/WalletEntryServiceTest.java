package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.repositories.WalletRepository;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryPopulator;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryRepository;
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
public class WalletEntryServiceTest {

    private static final Long WALLET_ID = 3L;
    private static final Long ENTRY_ID = 1L;

    private WalletEntryService walletEntryService;

    @MockBean
    private WalletEntryRepository walletEntryRepository;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private WalletEntryPopulator walletEntryPopulator;


    @Before
    public void setUp() {
        walletEntryService = new WalletEntryService(walletEntryRepository, walletEntryPopulator, walletRepository);
    }

    @Test
    public void getListByWalletId_test() {
        var data1 = mock(WalletEntryData.class);
        var data2 = mock(WalletEntryData.class);

        var model1 = mock(WalletEntryModel.class);
        var model2 = mock(WalletEntryModel.class);

        given(walletEntryRepository.findAllByWalletId(WALLET_ID)).willReturn(Arrays.asList(model1, model2));
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model1))).willReturn(data1);
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model2))).willReturn(data2);

        var result = walletEntryService.getListByWalletId(WALLET_ID);
        assertThat(result, containsInAnyOrder(data1, data2));
    }

    @Test
    public void findByIdAndWalletId_shouldReturnOptionalWithPresentData_whenFound() {
        var data = mock(WalletEntryData.class);
        var model = mock(WalletEntryModel.class);

        given(walletEntryRepository.findByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(Optional.of(model));
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model))).willReturn(data);

        var result = walletEntryService.findByIdAndWalletId(ENTRY_ID, WALLET_ID);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), data);
    }

    @Test
    public void findByIdAndWalletId_shouldReturnEmptyOptional_whenNotFound() {
        given(walletEntryRepository.findByIdAndWalletId(any(), any())).willReturn(Optional.empty());

        var result = walletEntryService.findByIdAndWalletId(ENTRY_ID, WALLET_ID);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }

    @Test
    public void createEntryToWallet_shouldReturnEmptyOptional_whenWalletDoesNotExist() {
        var data = mock(WalletEntryData.class);

        given(walletRepository.existsById(WALLET_ID)).willReturn(false);

        var result = walletEntryService.createEntryToWallet(WALLET_ID, data);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }

    @Test
    public void createEntryToWallet_shouldPersist_whenWalletExists() {
        var data = mock(WalletEntryData.class);
        var model = mock(WalletEntryModel.class);

        given(walletRepository.existsById(WALLET_ID)).willReturn(true);
        given(walletEntryPopulator.populateModel(any(WalletEntryModel.class), eq(data))).willReturn(model);
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), nullable(WalletEntryModel.class)))
                .willReturn(data);

        walletEntryService.createEntryToWallet(WALLET_ID, data);

        verify(walletEntryRepository).save(model);
    }

    @Test
    public void createEntryToWallet_shouldReturnData_whenWalletExists() {
        var inputData = mock(WalletEntryData.class);
        var expected = mock(WalletEntryData.class);
        var model1 = mock(WalletEntryModel.class);
        var model2 = mock(WalletEntryModel.class);

        given(walletRepository.existsById(WALLET_ID)).willReturn(true);
        given(walletEntryPopulator.populateModel(any(WalletEntryModel.class), eq(inputData))).willReturn(model1);
        given(walletEntryRepository.save(model1)).willReturn(model2);
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), eq(model2))).willReturn(expected);

        var result = walletEntryService.createEntryToWallet(WALLET_ID, inputData);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), expected);
    }

    @Test
    public void deleteByIdAndWalletId_shouldNotDelete_andShouldReturnFalse_whenRecordDoesNotExist() {
        given(walletEntryRepository.existsByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(false);

        var result = walletEntryService.deleteByIdAndWalletId(ENTRY_ID, WALLET_ID);

        assertFalse(result);
        verify(walletEntryRepository, never()).deleteByIdAndWalletId(any(), any());
    }

    @Test
    public void deleteByIdAndWalletId_shouldDelete_andShouldReturnTrue_whenRecordDoesExist() {
        given(walletEntryRepository.existsByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(true);

        var result = walletEntryService.deleteByIdAndWalletId(ENTRY_ID, WALLET_ID);

        assertTrue(result);
        verify(walletEntryRepository).deleteByIdAndWalletId(ENTRY_ID, WALLET_ID);
    }

    @Test
    public void updateById_shouldReturnOptionalWithPresentData_whenFoundAndUpdated() {
        var inputData = mock(WalletEntryData.class);
        var expected = mock(WalletEntryData.class);
        var model = mock(WalletEntryModel.class);

        given(walletEntryRepository.findByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(Optional.of(model));
        given(walletEntryPopulator.populateData(any(WalletEntryData.class), nullable(WalletEntryModel.class)))
                .willReturn(expected);

        var result = walletEntryService.updateByIdAndWalletId(ENTRY_ID, WALLET_ID, inputData);

        if (result.isEmpty()) {
            fail("Result should not be empty!");
        }

        assertEquals(result.get(), expected);
    }

    @Test
    public void updateById_shouldReturnEmptyOptional_whenNotFound() {
        var inputData = mock(WalletEntryData.class);

        given(walletEntryRepository.findByIdAndWalletId(ENTRY_ID, WALLET_ID)).willReturn(Optional.empty());

        var result = walletEntryService.updateByIdAndWalletId(ENTRY_ID, WALLET_ID, inputData);

        if (result.isPresent()) {
            fail("Result should be empty!");
        }
    }
}
