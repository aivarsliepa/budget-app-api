package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.constants.URLPaths;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.services.WalletEntryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public WalletEntryData createEntryToWallet(@PathVariable Long walletId, @Valid @RequestBody WalletEntryData data) {
        return walletEntryService.createEntryToWallet(walletId, data);
    }

    @GetMapping("/{entryId}")
    public WalletEntryData findByIdAndWalletId(@PathVariable Long entryId, @PathVariable Long walletId) {
        return walletEntryService.findByIdAndWalletId(entryId, walletId);
    }

    @PostMapping("/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateByIdAndWalletId(@Valid @RequestBody WalletEntryData data, @PathVariable Long entryId,
                                      @PathVariable Long walletId) {
        walletEntryService.updateByIdAndWalletId(entryId, walletId, data);
    }

    @DeleteMapping("/{entryId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByIdAndWalletId(@PathVariable Long entryId, @PathVariable Long walletId) {
        walletEntryService.deleteByIdAndWalletId(entryId, walletId);
    }
}
