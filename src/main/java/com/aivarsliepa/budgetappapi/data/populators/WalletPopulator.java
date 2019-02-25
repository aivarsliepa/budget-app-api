package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.dto.WalletData;
import com.aivarsliepa.budgetappapi.data.models.WalletModel;

public class WalletPopulator implements Populator<WalletModel, WalletData> {
    @Override
    public WalletData populateData(WalletData target, WalletModel source) {
        target.setId(source.getId());
        target.setName(source.getName());

        return target;
    }

    @Override
    public WalletModel populateModel(WalletModel target, WalletData source) {
        if (null != source.getName()) {
            target.setName(source.getName());
        }

        return target;
    }
}
