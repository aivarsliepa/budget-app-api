package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.wallet.WalletData;
import com.aivarsliepa.budgetappapi.data.wallet.WalletModel;
import com.aivarsliepa.budgetappapi.data.wallet.WalletPopulator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class WalletPopulatorTest {

    private WalletPopulator populator = new WalletPopulator();

    private final String NAME = "name";
    private final Long ID = 1L;

    @Test
    public void shouldMapDataToModel() {
        var data = new WalletData();
        data.setName(NAME);

        var model = populator.populateModel(new WalletModel(), data);

        assertEquals(NAME, model.getName());
    }

    @Test
    public void shouldMapModelToData() {
        var model = new WalletModel();
        model.setName(NAME);
        model.setId(ID);

        var data = populator.populateData(new WalletData(), model);

        assertEquals(NAME, data.getName());
        assertEquals(ID, data.getId());
    }

    @Test
    public void populateModel_shouldNotSetId() {
        var model = new WalletModel();
        model.setId(ID);

        var data = new WalletData();
        data.setId(123L);

        populator.populateModel(model, data);

        assertEquals(ID, model.getId());
    }

    @Test
    public void populateModel_shouldReturnSameModel() {
        var model = mock(WalletModel.class);

        var result = populator.populateModel(model, new WalletData());

        assertEquals(result, model);
    }
}
