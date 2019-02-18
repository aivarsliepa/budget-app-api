package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.models.Income;
import com.aivarsliepa.budgetappapi.repositories.IncomeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets/{walletId}/income")
@RequiredArgsConstructor
public class IncomeController {
    @NonNull
    private IncomeRepository incomeRepository;

    @GetMapping
    public List<Income> getIncomeList(@PathVariable final Long walletId) {
        return incomeRepository.findAllByWalletId(walletId);
    }

    @PostMapping
    public Income postIncome(@RequestBody final Income income) {
        return incomeRepository.save(income);
    }

    @DeleteMapping
    public void deleteIncome(@RequestBody final Income income) {
        incomeRepository.delete(income);
    }

    @GetMapping("/{incomeId}")
    public ResponseEntity<Income> getIncome(@PathVariable final Long incomeId, @PathVariable final Long walletId) {
        return incomeRepository.findByIdAndWalletId(incomeId, walletId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{incomeId}")
    public ResponseEntity<Income> updateIncome(@RequestBody final Income income, @PathVariable final Long incomeId,
                                                 @PathVariable final Long walletId) {
        var exists = incomeRepository.existsByIdAndWalletId(incomeId, walletId);

        if (exists) {
            income.setWalletId(walletId);
            income.setId(incomeId);
            var persistedIncome = incomeRepository.save(income);
            return ResponseEntity.ok(persistedIncome);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity deleteIncome(@PathVariable final Long incomeId, @PathVariable final Long walletId) {
        var exists = incomeRepository.existsByIdAndWalletId(incomeId, walletId);

        if (exists) {
            incomeRepository.deleteById(incomeId);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
