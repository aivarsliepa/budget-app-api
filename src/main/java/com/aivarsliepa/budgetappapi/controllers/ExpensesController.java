package com.aivarsliepa.budgetappapi.controllers;

import com.aivarsliepa.budgetappapi.data.models.Expense;
import com.aivarsliepa.budgetappapi.data.repositories.ExpenseRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets/{walletId}/expenses")
@RequiredArgsConstructor
public class ExpensesController {

    @NonNull
    private ExpenseRepository expensesRepository;

    @GetMapping
    public List<Expense> getExpenseList(@PathVariable final Long walletId) {
        return expensesRepository.findAllByWalletId(walletId);
    }

    @PostMapping
    public Expense postExpense(@RequestBody final Expense expense) {
        return expensesRepository.save(expense);
    }

    @DeleteMapping
    public void deleteExpense(@RequestBody final Expense expense) {
        expensesRepository.delete(expense);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> getExpense(@PathVariable final Long expenseId, @PathVariable final Long walletId) {
        return expensesRepository.findByIdAndWalletId(expenseId, walletId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@RequestBody final Expense expense, @PathVariable final Long expenseId,
                                                 @PathVariable final Long walletId) {
        var exists = expensesRepository.existsByIdAndWalletId(expenseId, walletId);

        if (exists) {
            expense.setWalletId(walletId);
            expense.setId(expenseId);
            var persistedExpense = expensesRepository.save(expense);
            return ResponseEntity.ok(persistedExpense);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity deleteExpense(@PathVariable final Long expenseId, @PathVariable final Long walletId) {
        var exists = expensesRepository.existsByIdAndWalletId(expenseId, walletId);

        if (exists) {
            expensesRepository.deleteById(expenseId);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
