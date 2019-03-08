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
    private final Long USER_ID = 2L;

    @Test
    public void shouldMapDataToModel() {
        var model = new WalletModel();
        model.setUserId(USER_ID);


        var data = new WalletData();
        data.setName(NAME);
        data.setId(ID);

        var expected = new WalletModel();
        expected.setUserId(USER_ID);
        expected.setName(NAME);

        populator.populateModel(model, data);

        assertEquals(model, expected);
    }

    @Test
    public void shouldMapModelToData() {
        var model = new WalletModel();
        model.setName(NAME);
        model.setId(ID);

        var expected = new WalletData();
        expected.setName(NAME);
        expected.setId(ID);

        var result = populator.populateData(new WalletData(), model);

        assertEquals(result, expected);
    }

    @Test
    public void populateModel_shouldNotSetId() {
        var model = new WalletModel();
        model.setId(ID);

        var data = new WalletData();
        data.setId(123L);

        var expected = new WalletModel();
        expected.setId(ID);

        populator.populateModel(model, data);

        assertEquals(model, expected);
    }

    @Test
    public void populateModel_shouldReturnSameModel() {
        var model = mock(WalletModel.class);

        var result = populator.populateModel(model, new WalletData());

        assertEquals(result, model);
    }
}
