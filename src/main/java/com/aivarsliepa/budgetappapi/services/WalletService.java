package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.dto.WalletData;
import com.aivarsliepa.budgetappapi.data.models.WalletModel;
import com.aivarsliepa.budgetappapi.data.populators.WalletPopulator;
import com.aivarsliepa.budgetappapi.data.repositories.WalletRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WalletService {
    @NonNull
    private WalletRepository walletRepository;

    @NonNull
    private WalletPopulator walletPopulator;

    public List<WalletData> getList() {
        return walletRepository
                .findAll()
                .stream()
                .map(model -> walletPopulator.populateData(new WalletData(), model))
                .collect(Collectors.toList());
    }

    public WalletData create(WalletData walletData) {
        var model = walletPopulator.populateModel(new WalletModel(), walletData);

        var persistedModel = walletRepository.save(model);

        return walletPopulator.populateData(new WalletData(), persistedModel);
    }

    public boolean deleteById(Long id) {
        if (!walletRepository.existsById(id)) {
            return false;
        }

        walletRepository.deleteById(id);
        return true;
    }

    public Optional<WalletData> findById(Long id) {
        return walletRepository
                .findById(id)
                .map((model -> walletPopulator.populateData(new WalletData(), model)));
    }

    public Optional<WalletData> updateById(Long id, WalletData categoryData) {
        return walletRepository.findById(id).map(model -> {
            walletPopulator.populateModel(model, categoryData);
            var updatedModel = walletRepository.save(model);
            return walletPopulator.populateData(new WalletData(), updatedModel);
        });
    }
}
