package ru.itis.dao.categoryDishDao;

import com.google.common.collect.Lists;
import ru.itis.models.CategoryDish;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDishJdbcTemlateImpl implements CategoryDishDao {


    private final static String
            SQL_INSERT = "INSERT INTO category_dish(name) VALUES (?);",
            SQL_SELECT_BY_ID = "SELECT * FROM category_dish WHERE id = ?;",
            SQL_SELECT_ALL = "SELECT * FROM category_dish;",
            SQL_UPDATE = "UPDATE category_dish SET name = ?;",
            SQL_DELETE = "DELETE FROM category_dish WHERE id = ?;";
    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Map<Long , CategoryDish> categoryDishes;

    public CategoryDishJdbcTemlateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.categoryDishes = new HashMap<Long, CategoryDish>();
    }
    private RowMapper<CategoryDish> categoryDishRowMapper = ((resultSet, rowNumber) -> {
        Long currentCategoryDishId = resultSet.getLong(1);

        if (categoryDishes.get(currentCategoryDishId) == null) {
            categoryDishes.put(currentCategoryDishId, CategoryDish.builder()
                    .id(currentCategoryDishId)
                    .name(resultSet.getString(2))
                    .build());
        }

        return categoryDishes.get(currentCategoryDishId);
    });

    @Override
    public List<CategoryDish> findAll() {
        template.query(SQL_SELECT_ALL, categoryDishRowMapper);
        List<CategoryDish> result = Lists.newArrayList(categoryDishes.values());
        categoryDishes.clear();

        return result;
    }

    @Override
    public void save(CategoryDish model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, new String[]{"id"});
            statement.setString(1, model.getName());

            return statement;
        }, keyHolder);

        model.setId(keyHolder.getKey().longValue());
    }

    @Override
    public CategoryDish find(Long id) {
        CategoryDish result = template.query(SQL_SELECT_BY_ID, new Long[]{id}, categoryDishRowMapper).get(0);
        categoryDishes.clear();

        return result;
    }

    @Override
    public void delete(Long id) {
        template.update(SQL_DELETE, id);
    }

    @Override
    public void update(CategoryDish model, Long id) {
        template.update(SQL_UPDATE,
                model.getName(),id);
    }
}
