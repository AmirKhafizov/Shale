package ru.itis.dao.userDao;

import com.google.common.collect.Lists;
import ru.itis.models.User;
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

public class UserDaoJdbcTemplateImpl implements UserDao {

    private final static String
            SQL_INSERT_USER = "INSERT INTO user(login , password) VALUES (?,?);",
            SQL_SELECT_USER_BY_ID = "SELECT * FROM user WHERE id = ?;",
            SQL_SELECT_ALL = "SELECT * FROM user;",
            SQL_UPDATE = "UPDATE user SET login = ?, password = ?;",
            SQL_DELETE = "DELETE FROM user WHERE id = ?;";

    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Map<Long , User> users;

    public UserDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.users = new HashMap<Long, User>();
    }

    private RowMapper<User> userRowMapper = ((resultSet, rowNumber) -> {
        Long currentUserId = resultSet.getLong(1);

        if (users.get(currentUserId) == null) {
            users.put(currentUserId, User.builder()
                    .id(currentUserId)
                    .login(resultSet.getString(2))
                    .password(resultSet.getString(3))
                    .build());
        }

        return users.get(currentUserId);
    });
    @Override
    public List<User> findAll() {
        List<User> result = Lists.newArrayList(users.values());
        template.query(SQL_SELECT_ALL, userRowMapper);
        users.clear();

        return result;
    }

    @Override
    public void save(User model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER, new String[]{"id"});
            statement.setString(1, model.getLogin());
            statement.setString(2, model.getPassword());

            return statement;
        }, keyHolder);

        model.setId(keyHolder.getKey().longValue());
    }

    @Override
    public User find(Long id) {
        User result = template.query(SQL_SELECT_USER_BY_ID, new Long[]{id}, userRowMapper).get(0);
        users.clear();

        return result;
    }

    @Override
    public void delete(Long id) {
        template.update(SQL_DELETE, id);
    }

    @Override
    public void update(User model , Long id) {
        template.update(SQL_UPDATE,
                model.getLogin(),
                model.getPassword(),
                id);
    }
}
