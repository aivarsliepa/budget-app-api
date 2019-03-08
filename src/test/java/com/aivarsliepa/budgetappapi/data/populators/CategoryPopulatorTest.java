package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.category.CategoryData;
import com.aivarsliepa.budgetappapi.data.category.CategoryModel;
import com.aivarsliepa.budgetappapi.data.category.CategoryPopulator;
import com.aivarsliepa.budgetappapi.data.common.enums.CategoryType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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

        var result = populator.populateModel(new CategoryModel(), data);

        assertEquals(PARENT_ID, result.getParentId());
        assertEquals(TYPE, result.getType());
    }

    @Test
    public void shouldMapModelToData() {
        var model = new CategoryModel();
        model.setParentId(PARENT_ID);
        model.setType(TYPE);
        model.setId(ID);

        var result = populator.populateData(new CategoryData(), model);

        assertEquals(PARENT_ID, result.getParentId());
        assertEquals(TYPE, result.getType());
        assertEquals(ID, result.getId());
    }

    @Test
    public void populateModel_shouldNotSetId() {
        var data = new CategoryData();
        data.setId(12123123123L);

        var model = new CategoryModel();
        model.setId(ID);

        populator.populateModel(model, data);

        assertEquals(ID, model.getId());
    }

    @Test
    public void populateModel_shouldReturnSameModel() {
        var model = mock(CategoryModel.class);

        var result = populator.populateModel(model, new CategoryData());

        assertEquals(result, model);
    }
}
