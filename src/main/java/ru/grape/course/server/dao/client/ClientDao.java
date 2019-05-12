package ru.grape.course.server.dao.client;

import ru.grape.course.server.dao.CrudDao;
import ru.grape.course.server.model.Account;
import ru.grape.course.server.model.Client;

import java.sql.SQLException;

public interface ClientDao extends CrudDao<Client> {
    Client getClientByAccountId(Long id) throws SQLException;

    void deleteClientByLogin(String login) throws SQLException;

    void addClientByAccount(Account account) throws SQLException;

    Client getByAccountId(long id) throws SQLException;
}
