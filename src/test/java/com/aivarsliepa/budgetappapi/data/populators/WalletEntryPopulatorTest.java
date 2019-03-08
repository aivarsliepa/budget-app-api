package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryData;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryModel;
import com.aivarsliepa.budgetappapi.data.walletentry.WalletEntryPopulator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class WalletEntryPopulatorTest {

    private WalletEntryPopulator populator = new WalletEntryPopulator();

    private final CategoryType TYPE = CategoryType.EXPENSE;
    private final String DESCRIPTION = "some description";
    private final Date DATE = new Date();
    private final Long CATEGORY_ID = 2L;
    private final Long ID = 1L;

    @Test
    public void shouldMapDataToModel() {
        var data = new WalletEntryData();
        data.setCategoryId(CATEGORY_ID);
        data.setDescription(DESCRIPTION);
        data.setDate(DATE);
        data.setType(TYPE);

        var result = populator.populateModel(new WalletEntryModel(), data);

        assertEquals(CATEGORY_ID, result.getCategoryId());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(DATE, result.getDate());
        assertEquals(TYPE, result.getType());
    }

    @Test
    public void shouldMapModelToData() {
        var model = new WalletEntryModel();
        model.setCategoryId(CATEGORY_ID);
        model.setDescription(DESCRIPTION);
        model.setType(TYPE);
        model.setDate(DATE);
        model.setId(ID);

        var result = populator.populateData(new WalletEntryData(), model);

        assertEquals(CATEGORY_ID, result.getCategoryId());
        assertEquals(DESCRIPTION, result.getDescription());
        assertEquals(TYPE, result.getType());
        assertEquals(DATE, result.getDate());
        assertEquals(ID, result.getId());
    }

    @Test
    public void populateModel_shouldNotSetId() {
        var model = new WalletEntryModel();
        model.setId(ID);

        var data = new WalletEntryData();
        data.setId(123L);

        populator.populateModel(model, data);

        assertEquals(ID, model.getId());
    }

    @Test
    public void populateModel_shouldReturnSameModel() {
        var model = mock(WalletEntryModel.class);

        var result = populator.populateModel(model, new WalletEntryData());

        assertEquals(result, model);
    }
}
