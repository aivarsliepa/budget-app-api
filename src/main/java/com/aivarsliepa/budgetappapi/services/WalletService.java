package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import com.aivarsliepa.budgetappapi.data.wallet.WalletPopulator;
import com.aivarsliepa.budgetappapi.data.wallet.WalletRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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

    public Optional<WalletData> updateById(Long id, WalletData data) {
        return walletRepository.findById(id).map(model -> {
            walletPopulator.populateModel(model, data);
            var updatedModel = walletRepository.save(model);
            return walletPopulator.populateData(new WalletData(), updatedModel);
        });
    }
}
