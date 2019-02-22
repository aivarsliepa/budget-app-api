package com.aivarsliepa.budgetappapi.data.populators;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CategoryPopulatorTest {

    private CategoryPopulator populator = new CategoryPopulator();

    private CategoryData data;
    private CategoryModel model;

    private CategoryType type = CategoryType.EXPENSE;
    private Long parentId = 2L;
    private Long id = 1L;

    private CategoryData createData() {
        var data = new CategoryData();
        data.setId(id);
        data.setParentId(parentId);
        data.setCategoryType(type);
        return data;
    }

    private CategoryModel createModel() {
        var model = new CategoryModel();
        model.setCategoryType(type);
        model.setParentId(parentId);
        model.setId(id);
        return model;
    }

    @Before
    public void beforeEach() {
        data = createData();
        model = createModel();
    }

    @Test
    public void shouldMapDataToModel() {
        populator.populateModel(model, data);

        assertEquals(model, createModel());
    }

    @Test
    public void shouldMapModelToData() {
        populator.populateData(data, model);

        assertEquals(data, createData());
    }

    @Test
    public void populateModel_shouldNotSetType_whenNull() {
        data.setType(null);

        populator.populateModel(model, data);

        assertEquals(model, createModel());
    }

    @Test
    public void populateModel_shouldNotSetParentId_whenNull() {
        data.setParentId(null);

        populator.populateModel(model, data);

        assertEquals(model, createModel());
    }

    @Test
    public void populateModel_shouldNotSetId() {
        data.setId(1231231L);

        populator.populateModel(model, data);

        assertEquals(model, createModel());
    }
}
