package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.services.WalletService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(URLPaths.Wallets.BASE)
@RequiredArgsConstructor
public class WalletController {
    @NonNull
    private WalletService walletService;

    @GetMapping
    public List<WalletData> getList() {
        return walletService.getListForCurrentUser();
    }

    @PostMapping
    public WalletData create(@Valid @RequestBody final WalletData wallet) {
        return walletService.create(wallet);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletData> findById(@PathVariable final Long walletId) {
        return walletService.findById(walletId)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateById(@Valid @RequestBody final WalletData wallet, @PathVariable final Long walletId) {
        walletService.updateById(walletId, wallet);
    }

    @DeleteMapping("/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable final Long walletId) {
        walletService.deleteById(walletId);
    }
}
