package com.aivarsliepa.budgetappapi.data.mappers;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CategoryMapperTest {

    private CategoryMapper mapper = new CategoryMapper();

    private CategoryData data;
    private CategoryModel model;

    @Before
    public void beforeEach() {
        var type = CategoryType.EXPENSE;
        var parentId = 2L;
        var id = 1L;

        data = new CategoryData();
        data.setId(id);
        data.setParentId(parentId);
        data.setCategoryType(type);

        model = new CategoryModel();
        model.setCategoryType(type);
        model.setParentId(parentId);
        model.setId(id);
    }

    @Test
    public void shouldMapDataToModel() {

        var actual = mapper.map(data);

        assertEquals(model, actual);
    }

    @Test
    public void shouldMapModelToData() {

        var actual = mapper.map(model);

        assertEquals(data, actual);
    }
}
