package ru.grape.course.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {
    private Integer id;
    private String name;
    private String description;
    private Integer min_age;
    private Double price;
    private String image_url;

    public Exercise(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.description = resultSet.getString("description");
        this.min_age = resultSet.getInt("min_age");
        this.price = resultSet.getDouble("price");
        this.image_url = resultSet.getString("image_url");
    }
}
