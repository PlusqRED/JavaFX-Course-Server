package ru.grape.course.server.dao.rate;

import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.model.Rate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RateDaoImpl implements RateDao {
    private Datasource datasource = Datasource.getInstance();
    private static RateDaoImpl instance;

    //language=SQL
    private static final String UPDATE_BY_CLIENT_ID =
            "update public.rate set variety = ?, " +
                    "satisfaction = ?, " +
                    "exercise_time = ?, " +
                    "trainers_time = ?, " +
                    "quality = ? " +
                    "where client_id = ?";

    //language=SQL
    private static final String SAVE =
            "insert into public.rate values (DEFAULT, ?, ?, ?, ?, ?, ?)";

    //language=SQL
    private static final String GET_ALL =
            "select * from public.rate";

    //language=SQL
    private static final String GET_RATE_BY_CLIENT_ID =
            "select * from public.rate where client_id = ?";

    @Override
    public Rate getRateByClientId(long id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_RATE_BY_CLIENT_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            return new Rate(resultSet);
        }
        return null;
    }

    @Override
    public void update(Rate rate) throws SQLException {
        PreparedStatement statement = datasource.getStatement(UPDATE_BY_CLIENT_ID);
        statement.setInt(1, rate.getVariety());
        statement.setInt(2, rate.getSatisfaction());
        statement.setInt(3, rate.getExercise_time());
        statement.setInt(4, rate.getTrainers_time());
        statement.setInt(5, rate.getQuality());
        statement.setLong(6, rate.getClient().getId());
        if(statement.executeUpdate() == 0) {
            save(rate);
        }
    }

    @Override
    public Optional<Rate> get(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Rate> getAll() throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_ALL);
        ResultSet resultSet = statement.executeQuery();
        List<Rate> rates = new ArrayList<>();
        while (resultSet.next()) {
            rates.add(new Rate(resultSet));
        }
        return rates;
    }

    @Override
    public void save(Rate rate) throws SQLException {
        PreparedStatement statement = datasource.getStatement(SAVE);
        statement.setLong(1, rate.getClient().getId());
        statement.setInt(2, rate.getVariety());
        statement.setInt(3, rate.getSatisfaction());
        statement.setInt(4, rate.getExercise_time());
        statement.setInt(5, rate.getTrainers_time());
        statement.setInt(6, rate.getQuality());
        statement.executeUpdate();
    }

    @Override
    public void delete(Rate rate) throws SQLException {

    }

    private RateDaoImpl() {}

    public static RateDaoImpl getInstance() {
        if(instance == null) {
            synchronized (RateDaoImpl.class) {
                if(instance == null) {
                    instance = new RateDaoImpl();
                }
            }
        }
        return instance;
    }
}
