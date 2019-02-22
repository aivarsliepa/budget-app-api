package com.aivarsliepa.budgetappapi.data.mappers;

import com.aivarsliepa.budgetappapi.data.dto.CategoryData;
import com.aivarsliepa.budgetappapi.data.enums.CategoryType;
import com.aivarsliepa.budgetappapi.data.models.CategoryModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class CategoryMapperTest {

    private CategoryMapper mapper = new CategoryMapper();

    @Test
    public void shouldMapDataToModel() {
        var type = CategoryType.EXPENSE;
        var parentId = 2L;
        var id = 1L;

        var data = new CategoryData();
        data.setId(id);
        data.setParentId(parentId);
        data.setCategoryType(type);

        var expected = new CategoryModel();
        expected.setCategoryType(type);
        expected.setParentId(parentId);
        expected.setId(id);

        var actual = mapper.map(data);

        assertEquals(expected, actual);
    }

    @Test
    public void shouldMapModelToData() {
        var type = CategoryType.INCOME;
        var parentId = 1L;
        var id = 2L;

        var model = new CategoryModel();
        model.setCategoryType(type);
        model.setParentId(parentId);
        model.setId(id);

        var expected = new CategoryData();
        expected.setId(id);
        expected.setParentId(parentId);
        expected.setCategoryType(type);

        var actual = mapper.map(model);

        assertEquals(expected, actual);
    }
}
