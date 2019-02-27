package com.aivarsliepa.budgetappapi.data.walletentry;

import com.aivarsliepa.budgetappapi.data.common.Populator;
import org.springframework.stereotype.Component;

@Component
public class WalletEntryPopulator implements Populator<WalletEntryModel, WalletEntryData> {
    @Override
    public WalletEntryData populateData(WalletEntryData target, WalletEntryModel source) {
        target.setDescription(source.getDescription());
        target.setCategoryId(source.getCategoryId());
        target.setType(source.getType());
        target.setDate(source.getDate());
        target.setId(source.getId());

        return target;
    }

    @Override
    public WalletEntryModel populateModel(WalletEntryModel target, WalletEntryData source) {
        target.setDescription(source.getDescription());
        target.setCategoryId(source.getCategoryId());
        target.setType(source.getType());
        target.setDate(source.getDate());

        return target;
    }
}
