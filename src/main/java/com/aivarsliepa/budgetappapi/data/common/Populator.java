package com.aivarsliepa.budgetappapi.data.common;

public interface Populator<M, D> {
    D populateData(D target, M source);

    M populateModel(M target, D source);
}
