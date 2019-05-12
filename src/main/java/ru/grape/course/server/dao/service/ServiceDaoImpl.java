package ru.grape.course.server.dao.service;

import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.model.Service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceDaoImpl implements ServiceDao {
    //language=SQL
    private static final String SAVE_CLIENT_SERVICE =
            "insert into public.service values (DEFAULT, ?, ?, ?)";
    //language=SQL
    private static final String GET_BY_CLIENT_ID =
            "select * from public.service where client_id = ?";
    //language=SQL
    private static final String DELETE_CLIENT_SERVICE_BY_EXERCISE_ID =
            "delete from public.service where client_id = ? and exercise_id = ?";
    //language=SQL
    private static final String GET_ALL =
            "select * from public.service";

    //language=SQL
    private static final String DELETE_BY_CLIENT_ID =
            "delete from public.service where client_id = ?";

    private static ServiceDaoImpl instance;
    private final Datasource datasource = Datasource.getInstance();

    private ServiceDaoImpl() {
    }

    public static ServiceDaoImpl getInstance() {
        if (instance == null) {
            synchronized (ServiceDaoImpl.class) {
                if (instance == null) {
                    instance = new ServiceDaoImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Service> getByClientId(long clientId) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_BY_CLIENT_ID);
        statement.setLong(1, clientId);
        ResultSet resultSet = statement.executeQuery();
        List<Service> services = new ArrayList<>();
        while (resultSet.next()) {
            services.add(new Service(resultSet));
        }
        return services;
    }

    @Override
    public void deleteClientServiceByExerciseId(Long client_id, Integer exercise_id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(DELETE_CLIENT_SERVICE_BY_EXERCISE_ID);
        statement.setLong(1, client_id);
        statement.setInt(2, exercise_id);
        statement.executeUpdate();
    }

    @Override
    public void deleteByClientId(Long id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(DELETE_BY_CLIENT_ID);
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    @Override
    public Optional<Service> get(long id) {
        return Optional.empty();
    }

    @Override
    public List<Service> getAll() throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_ALL);
        ResultSet resultSet = statement.executeQuery();
        List<Service> services = new ArrayList<>();
        while (resultSet.next()) {
            services.add(new Service(resultSet));
        }
        return services;
    }

    @Override
    public void save(Service service) throws SQLException {
        PreparedStatement statement = datasource.getStatement(SAVE_CLIENT_SERVICE);
        statement.setLong(1, service.getClient().getId());
        statement.setInt(2, service.getExercise().getId());
        statement.setDate(3, Date.valueOf(service.getStart_date()));
        statement.executeUpdate();
    }

    @Override
    public void update(Service service) {

    }

    @Override
    public void delete(Service service) {

    }
}
