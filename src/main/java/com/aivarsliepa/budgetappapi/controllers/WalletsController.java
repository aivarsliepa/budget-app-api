package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.models.Wallet;
import com.aivarsliepa.budgetappapi.data.repositories.WalletRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletsController {
    @NonNull
    private WalletRepository walletRepository;

    @GetMapping
    public List<Wallet> getWalletList() {
        return walletRepository.findAll();
    }

    @PostMapping
    public Wallet postWallet(@RequestBody final Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @DeleteMapping
    public void deleteWallet(@RequestBody final Wallet wallet) {
        walletRepository.delete(wallet);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable final Long walletId) {
        return walletRepository.findById(walletId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{walletId}")
    public Wallet updateWallet(@RequestBody final Wallet wallet, @PathVariable final Long walletId) {
        wallet.setId(walletId);
        return walletRepository.save(wallet);
    }

    @DeleteMapping("/{walletId}")
    public void deleteWallet(@PathVariable final Long walletId) {
        walletRepository.deleteById(walletId);
    }
}
