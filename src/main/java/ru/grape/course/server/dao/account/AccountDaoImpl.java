package ru.grape.course.server.dao.account;

import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.model.Account;
import ru.grape.course.server.model.Role;
import ru.grape.course.server.utils.CustomHash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {
    private static AccountDaoImpl instance;
    private Datasource datasource = Datasource.getInstance();


    //language=SQL
    private static final String GET_BY_LOGIN =
            "select * from public.account where account.login = ?";

    //language=SQL
    private static final String GET_BY_ID =
            "select * from public.account where id = ?";

    //language=SQL
    private static final String UPDATE_ACCOUNT =
            "update public.account set login = ? where id = ?";

    //language=SQL
    private static final String GET_BY_ROLE =
            "select * from public.account where role_id = ?";

    //language=SQL
    private static final String DELETE_BY_ID =
            "delete from public.account where id = ?";

    //language=SQL
    private static final String SAVE =
            "insert into public.account values (DEFAULT, ?, ?, ?)";


    @Override
    public List<Account> getByRole(Role role) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_BY_ROLE);
        statement.setInt(1, role.getId());
        ResultSet resultSet = statement.executeQuery();
        List<Account> accounts = new ArrayList<>();
        while (resultSet.next()) {
            accounts.add(new Account(resultSet));
        }
        return accounts;
    }

    @Override
    public Account getByLogin(String login) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_BY_LOGIN);
        statement.setString(1, login);
        ResultSet accountSet = statement.executeQuery();
        if(accountSet.next()) {
            return new Account(accountSet);
        }
        return null;
    }

    @Override
    public Optional<Account> get(long id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_BY_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            return Optional.of(new Account(resultSet));
        }
        return Optional.empty();
    }

    @Override
    public List<Account> getAll() {
        return null;
    }

    @Override
    public void save(Account account) throws SQLException {
        PreparedStatement statement = datasource.getStatement(SAVE);
        statement.setString(1, account.getLogin());
        statement.setString(2, CustomHash.getHash(account.getPassword(), 32));
        statement.setInt(3, account.getRole().getId());
        statement.executeUpdate();
    }

    @Override
    public void update(Account account) throws SQLException {
        PreparedStatement statement = datasource.getStatement(UPDATE_ACCOUNT);
        statement.setString(1, account.getLogin());
        statement.setLong(2, account.getId());
        statement.executeUpdate();
    }

    @Override
    public void delete(Account account) throws SQLException {
        PreparedStatement statement = datasource.getStatement(DELETE_BY_ID);
        statement.setLong(1, account.getId());
        statement.executeUpdate();
    }

    private AccountDaoImpl() {}

    public static AccountDaoImpl getInstance() {
        if(instance == null) {
            synchronized (AccountDaoImpl.class) {
                if(instance == null) {
                    instance = new AccountDaoImpl();
                }
            }
        }
        return instance;
    }

}
