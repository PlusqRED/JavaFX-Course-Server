package ru.grape.course.server.dao.account;

import ru.grape.course.server.dao.CrudDao;
import ru.grape.course.server.model.Account;
import ru.grape.course.server.model.Role;

import java.sql.SQLException;
import java.util.List;

public interface AccountDao extends CrudDao<Account> {
    Account getByLogin(String login) throws SQLException;

    List<Account> getByRole(Role role) throws SQLException;
}
