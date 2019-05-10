package ru.grape.course.server.dao.exercise;

import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.model.Client;
import ru.grape.course.server.model.Exercise;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExerciseDaoImpl implements ExerciseDao {
    private Datasource datasource = Datasource.getInstance();
    private static ExerciseDaoImpl instance;

    //language=SQL
    private static final String FIND_ALL =
            "select * from public.exercise";

    //language=SQL
    private static final String GET_BY_ID =
            "select * from public.exercise where id = ?";

    @Override
    public Optional<Exercise> get(long id) throws SQLException {
        PreparedStatement statement = datasource.getStatement(GET_BY_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            return Optional.of(new Exercise(resultSet));
        }
        return Optional.empty();
    }

    @Override
    public List<Exercise> getAll() throws SQLException {
        List<Exercise> exercises = new ArrayList<>();
        PreparedStatement statement = datasource.getStatement(FIND_ALL);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            exercises.add(new Exercise(resultSet));
        }
        return exercises;
    }

    @Override
    public void save(Exercise exercise) {

    }

    @Override
    public void update(Exercise exercise) {

    }

    @Override
    public void delete(Exercise exercise) {

    }

    private ExerciseDaoImpl() {}

    public static ExerciseDaoImpl getInstance() {
        if(instance == null) {
            synchronized (ExerciseDaoImpl.class) {
                if(instance == null) {
                    instance = new ExerciseDaoImpl();
                }
            }
        }
        return instance;
    }
}
