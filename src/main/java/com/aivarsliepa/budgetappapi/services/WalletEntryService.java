package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.repositories.WalletRepository;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryPopulator;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<WalletEntryData> getListByWalletId(Long walletId) {
        return walletEntryRepository
                .findAllByWalletId(walletId)
                .stream()
                .map(model -> walletEntryPopulator.populateData(new WalletEntryData(), model))
                .collect(Collectors.toList());
    }


    public Optional<WalletEntryData> findByIdAndWalletId(Long entryId, Long walletId) {
        return walletEntryRepository
                .findByIdAndWalletId(entryId, walletId)
                .map((model -> walletEntryPopulator.populateData(new WalletEntryData(), model)));
    }

    public Optional<WalletEntryData> createEntryToWallet(Long walletId, WalletEntryData walletData) {
        if (!walletRepository.existsById(walletId)) {
            return Optional.empty();
        }

        var model = walletEntryPopulator.populateModel(new WalletEntryModel(), walletData);
        var persistedModel = walletEntryRepository.save(model);
        var persistedData = walletEntryPopulator.populateData(new WalletEntryData(), persistedModel);
        return Optional.of(persistedData);
    }

    public boolean deleteByIdAndWalletId(Long entryId, Long walletId) {
        if (!walletEntryRepository.existsByIdAndWalletId(entryId, walletId)) {
            return false;
        }

        walletEntryRepository.deleteByIdAndWalletId(entryId, walletId);
        return true;
    }

    public Optional<WalletEntryData> updateByIdAndWalletId(Long entryId, Long walletId, WalletEntryData data) {
        return walletEntryRepository
                .findByIdAndWalletId(entryId, walletId)
                .map(model -> {
                    walletEntryPopulator.populateModel(model, data);
                    var updatedModel = walletEntryRepository.save(model);
                    return walletEntryPopulator.populateData(new WalletEntryData(), updatedModel);
                });
    }
}
