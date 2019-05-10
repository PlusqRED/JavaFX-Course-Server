package ru.grape.course.server.dao.goal;

import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.model.Goal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class GoalDaoImpl implements GoalDao {
    private Datasource datasource = Datasource.getInstance();
    private static GoalDaoImpl instance;

    //language=SQL
    private static final String GET_ALL =
            "select * from public.goal";

    //language=SQL
    private final static String UPDATE_GOAL =
            "update public.goal set name = ?, priority = ? where id = ?";


    @Override
    public void updateGoals(List<Goal> goals) {
        AtomicInteger i = new AtomicInteger(1);
        goals.forEach(goal -> {
            try {
                PreparedStatement preparedStatement = datasource.getConnection().prepareStatement(UPDATE_GOAL);
                preparedStatement.setString(1, goal.getName());
                preparedStatement.setInt(2, goal.getPriority());
                preparedStatement.setInt(3, i.getAndIncrement());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Optional<Goal> get(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Goal> getAll() throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_ALL);
        ResultSet resultSet = statement.executeQuery();
        List<Goal> goals = new ArrayList<>();
        while (resultSet.next()) {
            goals.add(new Goal(resultSet));
        }
        return goals;
    }

    @Override
    public void save(Goal goal) throws SQLException {

    }

    @Override
    public void update(Goal goal) throws SQLException {

    }

    @Override
    public void delete(Goal goal) throws SQLException {

    }

    private GoalDaoImpl() {}

    public static GoalDaoImpl getInstance() {
        if(instance == null) {
            synchronized (GoalDaoImpl.class) {
                if(instance == null) {
                    instance = new GoalDaoImpl();
                }
            }
        }
        return instance;
    }
}
