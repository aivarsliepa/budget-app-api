package com.aivarsliepa.budgetappapi.data.wallet;

import com.aivarsliepa.budgetappapi.data.common.Populator;
import org.springframework.stereotype.Component;

@Component
public class WalletPopulator implements Populator<WalletModel, WalletData> {
    @Override
    public WalletData populateData(WalletData target, WalletModel source) {
        target.setId(source.getId());
        target.setName(source.getName());
        return target;
    }

    @Override
    public WalletModel populateModel(WalletModel target, WalletData source) {
        target.setName(source.getName());
        return target;
    }
}
