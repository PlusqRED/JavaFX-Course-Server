package ru.grape.course.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.grape.course.server.dao.Datasource;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rate {
    private Long id;
    private Client client;
    private Integer variety;
    private Integer satisfaction;
    private Integer exercise_time;
    private Integer trainers_time;
    private Integer quality;

    public Rate(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        Datasource.getInstance()
                .getClientDao()
                .get(resultSet.getLong("client_id"))
                .ifPresent(this::setClient);
        this.variety = resultSet.getInt("variety");
        this.satisfaction = resultSet.getInt("satisfaction");
        this.exercise_time = resultSet.getInt("exercise_time");
        this.trainers_time = resultSet.getInt("trainers_time");
        this.quality = resultSet.getInt("quality");
    }
}
