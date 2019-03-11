package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.wallet.WalletRepository;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryPopulator;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryRepository;
import com.aivarsliepa.budgetappapi.exceptions.ResourceNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WalletEntryService {
    @NonNull
    private WalletEntryRepository walletEntryRepository;

    @NonNull
    private WalletEntryPopulator walletEntryPopulator;

    @NonNull
    private WalletRepository walletRepository;

    @NonNull
    private AuthService authService;

    @NonNull
    private WalletService walletService;

    public List<WalletEntryData> getListByWalletId(Long walletId) {
        var userId = authService.getCurrentUserId();

        return walletEntryRepository
                .findAllByUserIdAndWalletId(userId, walletId)
                .stream()
                .map(model -> walletEntryPopulator.populateData(new WalletEntryData(), model))
                .collect(Collectors.toList());
    }


    public WalletEntryData findByIdAndWalletId(Long entryId, Long walletId) {
        var userId = authService.getCurrentUserId();

        return walletEntryRepository
                .findByIdAndUserIdAndWalletId(entryId, userId, walletId)
                .map((model -> walletEntryPopulator.populateData(new WalletEntryData(), model)))
                .orElseThrow(() -> buildResourceNotFoundException(entryId, userId, walletId));
    }

    // TODO: skip unit test until after hibernate behavior is tested
    public WalletEntryData createEntryToWallet(Long walletId, WalletEntryData walletData) {
        var userModel = authService.getCurrentUser();
//        var userId = userModel.getId();
//        var walletModel = walletService.findById(walletId);

        var model = walletEntryPopulator.populateModel(new WalletEntryModel(), walletData);
//        model.setUser(userModel);
//        model.setWallet()

        // TODO: test if hibernate would fetch model automatically when saving or it would be null
        model.setWalletId(walletId);
        model.setUser(userModel);

        var persistedModel = walletEntryRepository.save(model);

        return walletEntryPopulator.populateData(new WalletEntryData(), persistedModel);
    }

    public void deleteByIdAndWalletId(Long entryId, Long walletId) {
        var userId = authService.getCurrentUserId();

        if (!walletEntryRepository.existsByIdAndUserIdAndWalletId(entryId, userId, walletId)) {
            throw buildResourceNotFoundException(entryId, userId, walletId);
        }

        walletEntryRepository.deleteByIdAndUserIdAndWalletId(entryId, userId, walletId);
    }

    public void updateByIdAndWalletId(Long entryId, Long walletId, WalletEntryData data) {
        var userId = authService.getCurrentUserId();

        var model = walletEntryRepository
                .findByIdAndUserIdAndWalletId(entryId, userId, walletId)
                .orElseThrow(() -> buildResourceNotFoundException(entryId, userId, walletId));

        walletEntryPopulator.populateModel(model, data);
    }

    private ResourceNotFoundException buildResourceNotFoundException(Long entryId, Long userId, Long walletId) {
        var msg = "WalletEntry not found with ID: " + entryId + "; userID: " + userId + "; walletID: " + walletId;
        return new ResourceNotFoundException(msg);
    }
}
