package com.aivarsliepa.budgetappapi.services;

import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import com.aivarsliepa.budgetappapi.data.wallet.WalletPopulator;
import com.aivarsliepa.budgetappapi.data.wallet.WalletRepository;
import com.aivarsliepa.budgetappapi.exceptions.ResourceNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
            throw buildResourceNotFoundException(id, userId);
        }

        walletRepository.deleteByIdAndUserId(id, userId);
    }

    public WalletData findById(Long id) {
        var userId = authService.getCurrentUserId();

        return walletRepository
                .findByIdAndUserId(id, userId)
                .map((model -> walletPopulator.populateData(new WalletData(), model)))
                .orElseThrow(() -> buildResourceNotFoundException(id, userId));
    }

    public void updateById(Long id, WalletData data) {
        var userId = authService.getCurrentUserId();

        var model = walletRepository
                .findByIdAndUserId(id, userId)
                .orElseThrow(() -> buildResourceNotFoundException(id, userId));

        walletPopulator.populateModel(model, data);
    }

    private ResourceNotFoundException buildResourceNotFoundException(Long id, Long userId) {
        var msg = "wallet does not exist with ID: " + id + "; userID: " + userId;
        return new ResourceNotFoundException(msg);
    }
}
