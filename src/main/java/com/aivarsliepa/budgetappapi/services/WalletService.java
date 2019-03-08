package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import com.aivarsliepa.budgetappapi.data.wallet.WalletPopulator;
import com.aivarsliepa.budgetappapi.data.wallet.WalletRepository;
import com.aivarsliepa.budgetappapi.exceptions.ResourceNotFoundException;
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

    @NonNull
    private AuthService authService;

    public List<WalletData> getListForCurrentUser() {
        return walletRepository
                .findAllByUserId(authService.getCurrentUserId())
                .stream()
                .map(model -> walletPopulator.populateData(new WalletData(), model))
                .collect(Collectors.toList());
    }

    public WalletData create(WalletData walletData) {
        var model = walletPopulator.populateModel(new WalletModel(), walletData);
        model.setUserId(authService.getCurrentUserId());

        var persistedModel = walletRepository.save(model);

        return walletPopulator.populateData(new WalletData(), persistedModel);
    }

    public void deleteById(Long id) {
        var userId = authService.getCurrentUserId();

        if (!walletRepository.existsByIdAndUserId(id, userId)) {
            var msg = "Wallet not found for with id: " + id + "; for userId: " + userId;
            throw new ResourceNotFoundException(msg);
        }

        walletRepository.deleteByIdAndUserId(id, userId);
    }

    public Optional<WalletData> findById(Long id) {
        return walletRepository
                .findByIdAndUserId(id, authService.getCurrentUserId())
                .map((model -> walletPopulator.populateData(new WalletData(), model)));
    }

    public void updateById(Long id, WalletData data) {
        walletRepository
                .findByIdAndUserId(id, authService.getCurrentUserId())
                .ifPresentOrElse(
                        model -> walletPopulator.populateModel(model, data),
                        () -> {
                            var msg = data.toString() + " not found for userId: " + id;
                            throw new ResourceNotFoundException(msg);
                        }
                );
    }
}
