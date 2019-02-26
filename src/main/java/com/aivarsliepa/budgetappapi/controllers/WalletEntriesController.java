package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntry;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryRepository;
import com.aivarsliepa.budgetappapi.services.WalletEntryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets/{walletId}/entries")
@RequiredArgsConstructor
public class WalletEntriesController {
    @NonNull
    private WalletEntryService walletEntryService;

    @GetMapping
    public List<WalletEntryData> getList(@PathVariable final Long walletId) {
        // TODO: 404 scenario, if wallet not found ?? (maybe: => think about that)
        return walletEntryService.getListByWalletId(walletId);
    }

    @PostMapping
    public WalletEntry create(@RequestBody final WalletEntry income) {
//        return walletEntryService.save(income);
//        return walletEntryService.createEntryToWallet(income);

        return null;
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<WalletEntry> findById(@PathVariable final Long entryId, @PathVariable final Long walletId) {
//        return walletEntryService.findByIdAndWalletId(entryId, walletId)
//                                    .map(ResponseEntity::ok)
//                                    .orElse(ResponseEntity.notFound().build());
        return null;
    }

    @PostMapping("/{entryId}")
    public ResponseEntity<WalletEntry> findById(@RequestBody final WalletEntry income, @PathVariable final Long entryId,
                                                @PathVariable final Long walletId) {
//        var exists = walletEntryService.existsByIdAndWalletId(entryId, walletId);
//
//        if (exists) {
//            income.setWalletId(walletId);
//            income.setId(entryId);
//            var persistedWalletEntry = walletEntryService.save(income);
//            return ResponseEntity.ok(persistedWalletEntry);
//        }
//
//        return ResponseEntity.notFound().build();
        return null;
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity deleteById(@PathVariable final Long entryId, @PathVariable final Long walletId) {
//        var exists = walletEntryService.existsByIdAndWalletId(entryId, walletId);
//
//        if (exists) {
//            walletEntryService.deleteById(entryId);
//            return ResponseEntity.ok().build();
//        }
//
//        return ResponseEntity.notFound().build();
        return null;
    }
}
