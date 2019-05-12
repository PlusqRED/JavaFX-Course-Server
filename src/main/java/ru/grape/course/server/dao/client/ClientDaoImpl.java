package ru.grape.course.server.dao.client;

import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.model.Account;
import ru.grape.course.server.model.Client;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {
    //language=SQL
    private static final String GET_CLIENT_BY_ACCOUNT_ID =
            "select * from public.client where accountid = ?";
    //language=SQL
    private static final String UPDATE_CLIENT =
            "update public.client set name = ?, surname = ?, city = ?, phone = ?, weight = ?, height = ? where id = ?";
    //language=SQL
    private static final String GET_BY_ID =
            "select * from public.client where id = ?";
    //language=SQL
    private static final String DELETE_CLIENT_BY_ACCOUNT_ID =
            "delete from public.client where accountid = ?";
    //language=SQL
    private static final String ADD_EMPTY_CLIENT =
            "insert into public.client values (DEFAULT, NULL, NULL, NULL, NULL, NULL, NULL, ?, NULL)";
    //language=SQL
    private static final String LAST_ACCOUNT_INDEX =
            "select id from public.account order by id desc limit 1";
    //language=SQL
    private static final String SAVE =
            "insert into public.client values (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
    //language=SQL
    private static final String GET_ALL =
            "select * from public.client";

    //language=SQL
    private static final String GET_BY_ACCOUNT_ID =
            "select * from public.client where accountid = ?";

    private static ClientDaoImpl instance;
    private final Datasource datasource = Datasource.getInstance();

    private ClientDaoImpl() {
    }

    public static ClientDaoImpl getInstance() {
        if (instance == null) {
            synchronized (ClientDaoImpl.class) {
                if (instance == null) {
                    instance = new ClientDaoImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void save(Client client) throws SQLException {
        datasource.getAccountDao().save(client.getAccount());
        PreparedStatement statement = datasource.getStatement(SAVE);
        statement.setString(1, client.getName());
        statement.setString(2, client.getSurname());
        statement.setString(3, client.getCity());
        statement.setLong(4, client.getPhone() == null ? 0 : client.getPhone());
        statement.setDouble(5, client.getWeight() == null ? 0 : client.getWeight());
        statement.setDouble(6, client.getHeight() == null ? 0 : client.getHeight());
        statement.setLong(7, getLastAccountIndex());
        statement.setDate(8, client.getBirthday() == null ? null : Date.valueOf(client.getBirthday()));
        statement.executeUpdate();
    }

    private long getLastAccountIndex() throws SQLException {
        ResultSet resultSet = datasource.getStatement(LAST_ACCOUNT_INDEX).executeQuery();
        resultSet.next();
        return resultSet.getLong("id");
    }

    @Override
    public void addClientByAccount(Account account) throws SQLException {
        datasource.getAccountDao().save(account);
        PreparedStatement statement = datasource.getStatement(ADD_EMPTY_CLIENT);
        statement.setLong(1, getLastAccountIndex());
        statement.executeUpdate();
    }

    @Override
    public Client getByAccountId(long id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_BY_ACCOUNT_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            return new Client(resultSet);
        }
        return null;
    }

    @Override
    public void deleteClientByLogin(String login) throws SQLException {
        Account account = datasource.getAccountDao().getByLogin(login);
        Client client = getByAccountId(account.getId());
        datasource.getRateDao().deleteByClientId(client.getId());
        datasource.getServiceDao().deleteByClientId(client.getId());
        PreparedStatement statement = datasource.getStatement(DELETE_CLIENT_BY_ACCOUNT_ID);
        statement.setLong(1, account.getId());
        statement.executeUpdate();
        datasource.getAccountDao().delete(account);
    }

    @Override
    public void update(Client client) throws SQLException {
        PreparedStatement statement = datasource.getStatement(UPDATE_CLIENT);
        statement.setString(1, client.getName());
        statement.setString(2, client.getSurname());
        statement.setString(3, client.getCity());
        statement.setLong(4, client.getPhone());
        statement.setDouble(5, client.getWeight());
        statement.setDouble(6, client.getHeight());
        statement.setLong(7, client.getId());
        statement.executeUpdate();
        datasource.getAccountDao().update(client.getAccount());
    }

    @Override
    public Client getClientByAccountId(Long id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_CLIENT_BY_ACCOUNT_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Client(resultSet);
        }
        return null;
    }

    @Override
    public Optional<Client> get(long id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_BY_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(new Client(resultSet));
        }
        return Optional.empty();
    }

    @Override
    public List<Client> getAll() throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_ALL);
        ResultSet resultSet = statement.executeQuery();
        List<Client> clients = new ArrayList<>();
        while (resultSet.next()) {
            clients.add(new Client(resultSet));
        }
        return clients;
    }

    @Override
    public void delete(Client client) {

    }
}
