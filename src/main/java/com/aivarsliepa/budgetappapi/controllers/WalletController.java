package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.services.WalletService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
        return walletService.getList();
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
    public ResponseEntity<WalletData> updateById(@Valid @RequestBody final WalletData wallet,
                                                 @PathVariable final Long walletId) {
        return walletService.updateById(walletId, wallet)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity deleteById(@PathVariable final Long walletId) {
        var found = walletService.deleteById(walletId);

        if (found) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
