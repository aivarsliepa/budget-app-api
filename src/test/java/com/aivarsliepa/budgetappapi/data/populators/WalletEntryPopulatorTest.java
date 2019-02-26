package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryPopulator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class WalletEntryPopulatorTest {

    private WalletEntryPopulator populator = new WalletEntryPopulator();

    private final CategoryType TYPE = CategoryType.EXPENSE;
    private final String DESCRIPTION = "some description";
    private final Date DATE = new Date();
    private final Long CATEGORY_ID = 2L;
    private final Long ID = 1L;
    private final Long WALLET_ID = 3L;

    @Test
    public void shouldMapDataToModel() {
        var model = new WalletEntryModel();
        model.setWalletId(WALLET_ID);

        var data = new WalletEntryData();
        data.setCategoryId(CATEGORY_ID);
        data.setDescription(DESCRIPTION);
        data.setDate(DATE);
        data.setType(TYPE);

        var expected = new WalletEntryModel();
        expected.setCategoryId(CATEGORY_ID);
        expected.setDescription(DESCRIPTION);
        expected.setDate(DATE);
        expected.setType(TYPE);
        expected.setWalletId(WALLET_ID);

        var actual = populator.populateModel(model, data);

        assertEquals(actual, expected);
    }

    @Test
    public void shouldMapModelToData() {
        var model = new WalletEntryModel();
        model.setCategoryId(CATEGORY_ID);
        model.setDescription(DESCRIPTION);
        model.setType(TYPE);
        model.setDate(DATE);
        model.setId(ID);

        var expected = new WalletEntryData();
        expected.setCategoryId(CATEGORY_ID);
        expected.setDescription(DESCRIPTION);
        expected.setType(TYPE);
        expected.setDate(DATE);
        expected.setId(ID);

        var actual = populator.populateData(new WalletEntryData(), model);

        assertEquals(actual, expected);
    }

    @Test
    public void populateModel_shouldNotSetId() {
        var model = new WalletEntryModel();
        model.setId(ID);

        var data = new WalletEntryData();
        data.setId(123L);

        var expected = new WalletEntryModel();
        expected.setId(ID);

        populator.populateModel(model, data);

        assertEquals(model, expected);
    }
}
