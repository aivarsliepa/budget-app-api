package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.services.WalletEntryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(URLPaths.Wallets.BASE + "/{walletId}" + URLPaths.Wallets.ENTRIES)
@RequiredArgsConstructor
public class WalletEntryController {
    @NonNull
    private WalletEntryService walletEntryService;

    @GetMapping
    public List<WalletEntryData> getListByWalletId(@PathVariable Long walletId) {
        return walletEntryService.getListByWalletId(walletId);
    }

    @PostMapping
    public ResponseEntity<WalletEntryData> createEntryToWallet(@PathVariable Long walletId,
                                                               @Valid @RequestBody WalletEntryData data) {
        return walletEntryService.createEntryToWallet(walletId, data)
                                 .map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<WalletEntryData> findByIdAndWalletId(@PathVariable Long entryId,
                                                               @PathVariable Long walletId) {
        return walletEntryService.findByIdAndWalletId(entryId, walletId)
                                 .map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{entryId}")
    public ResponseEntity<WalletEntryData> updateByIdAndWalletId(@Valid @RequestBody WalletEntryData data,
                                                                 @PathVariable Long entryId,
                                                                 @PathVariable Long walletId) {
        return walletEntryService.updateByIdAndWalletId(entryId, walletId, data)
                                 .map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity deleteByIdAndWalletId(@PathVariable Long entryId, @PathVariable Long walletId) {
        var exists = walletEntryService.deleteByIdAndWalletId(entryId, walletId);

        if (exists) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
