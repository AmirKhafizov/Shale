package ru.itis.dao.dishDao;

import com.google.common.collect.Lists;
import ru.itis.models.Dish;
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

public class DishDaoJdbcTemplateImpl implements DishDao {
    private final static String
            SQL_INSERT_USER = "INSERT INTO dish(name,description) VALUES (?,?);",
            SQL_SELECT_USER_BY_ID = "SELECT * FROM dish WHERE id = ?;",
            SQL_SELECT_ALL = "SELECT * FROM dish;",
            SQL_UPDATE = "UPDATE dish SET name = ?, description = ?;",
            SQL_DELETE = "DELETE FROM dish WHERE id = ?;";
    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Map<Long , Dish> dishes;

    public DishDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.dishes = new HashMap<Long, Dish>();
    }
    private RowMapper<Dish> dishRowMapper = ((resultSet, rowNumber) -> {
        Long currentDishId = resultSet.getLong(1);

        if (dishes.get(currentDishId) == null) {
            dishes.put(currentDishId, Dish.builder()
                    .id(currentDishId)
                    .name(resultSet.getString(2))
                    .description(resultSet.getString(3))
                    .build());
        }

        return dishes.get(currentDishId);
    });
    @Override
    public List<Dish> findAll() {
        List<Dish> result = Lists.newArrayList(dishes.values());
        template.query(SQL_SELECT_ALL, dishRowMapper);
        dishes.clear();

        return result;
    }

    @Override
    public void save(Dish model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER, new String[]{"id"});
            statement.setString(1, model.getName());
            statement.setString(2, model.getDescription());

            return statement;
        }, keyHolder);

        model.setId(keyHolder.getKey().longValue());
    }

    @Override
    public Dish find(Long id) {
        Dish result = template.query(SQL_SELECT_USER_BY_ID, new Long[]{id}, dishRowMapper).get(0);
        dishes.clear();

        return result;
    }

    @Override
    public void delete(Long id) {
        template.update(SQL_DELETE, id);
    }

    @Override
    public void update(Dish model, Long id) {
        template.update(SQL_UPDATE,
                model.getName(),
                model.getDescription(),id);
    }
}
