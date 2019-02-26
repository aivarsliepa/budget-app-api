package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CategoryPopulatorTest {

    private CategoryPopulator populator = new CategoryPopulator();

    private CategoryType TYPE = CategoryType.EXPENSE;
    private Long PARENT_ID = 2L;
    private Long ID = 1L;

    @Test
    public void shouldMapDataToModel() {
        var data = new CategoryData();
        data.setParentId(PARENT_ID);
        data.setType(TYPE);

        var expected = new CategoryModel();
        expected.setParentId(PARENT_ID);
        expected.setType(TYPE);

        var result = populator.populateModel(new CategoryModel(), data);

        assertEquals(result, expected);
    }

    @Test
    public void shouldMapModelToData() {
        var model = new CategoryModel();
        model.setParentId(PARENT_ID);
        model.setType(TYPE);
        model.setId(ID);

        var expected = new CategoryData();
        expected.setParentId(PARENT_ID);
        expected.setType(TYPE);
        expected.setId(ID);

        var result = populator.populateData(new CategoryData(), model);

        assertEquals(result, expected);
    }

    @Test
    public void populateModel_shouldNotSetId() {
        var data = new CategoryData();
        data.setId(12123123123L);

        var model = new CategoryModel();
        model.setId(ID);

        var expected = new CategoryModel();
        expected.setId(ID);

        populator.populateModel(model, data);

        assertEquals(model, expected);
    }
}
