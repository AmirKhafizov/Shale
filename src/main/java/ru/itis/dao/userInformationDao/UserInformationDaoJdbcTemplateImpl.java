package ru.itis.dao.userInformationDao;

import com.google.common.collect.Lists;
import ru.itis.models.UserInformation;
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

public class UserInformationDaoJdbcTemplateImpl implements UserInformationDao {

    private final static String
            SQL_INSERT_USER = "INSERT INTO user_infor(login , password) VALUES (?,?);",
            SQL_SELECT_BY_ID = "SELECT * FROM user WHERE id = ?;",
            SQL_SELECT_ALL = "SELECT * FROM user;",
            SQL_UPDATE = "UPDATE user SET login = ?, password = ?;",
            SQL_DELETE = "DELETE FROM user WHERE id = ?;";

    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Map<Long , UserInformation> userInformations;

    public UserInformationDaoJdbcTemplateImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.userInformations = new HashMap<Long, UserInformation>();
    }
    private RowMapper<UserInformation> userInformationRowMapper = ((resultSet, rowNumber) -> {
        Long currentUserInformationId = resultSet.getLong(1);

        if (userInformations.get(currentUserInformationId) == null) {
            userInformations.put(currentUserInformationId, UserInformation.builder()
                    .name(resultSet.getString(2))
                    .surname(resultSet.getString(3))
                    .build());
        }

        return userInformations.get(currentUserInformationId);
    });
    @Override
    public List<UserInformation> findAll() {
        List<UserInformation> result = Lists.newArrayList(userInformations.values());
        template.query(SQL_SELECT_ALL, userInformationRowMapper);
        userInformations.clear();

        return result;
    }

    @Override
    public void save(UserInformation model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USER, new String[]{"id"});
            statement.setString(1, model.getName());
            statement.setString(2, model.getSurname());

            return statement;
        }, keyHolder);

    }

    @Override
    public UserInformation find(Long id) {
        UserInformation result = template.query(SQL_SELECT_BY_ID, new Long[]{id},userInformationRowMapper).get(0);
        userInformations.clear();

        return result;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(UserInformation model, Long id) {

    }
}
