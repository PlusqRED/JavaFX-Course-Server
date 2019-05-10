package ru.grape.course.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.dao.client.ClientDao;
import ru.grape.course.server.dao.exercise.ExerciseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {
    private Long id;
    private Client client;
    private Exercise exercise;
    private LocalDate start_date;

    public Service(ResultSet resultSet) throws SQLException {
        ClientDao clientDao = Datasource.getInstance().getClientDao();
        ExerciseDao exerciseDao = Datasource.getInstance().getExerciseDao();
        this.id = resultSet.getLong("id");
        clientDao.get(resultSet.getLong("client_id")).ifPresent(this::setClient);
        exerciseDao.get(resultSet.getLong("exercise_id")).ifPresent(this::setExercise);
        start_date = resultSet.getDate("start_date").toLocalDate();
    }
}
