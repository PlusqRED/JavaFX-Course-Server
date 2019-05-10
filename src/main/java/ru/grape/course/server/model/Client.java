package ru.grape.course.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.grape.course.server.dao.Datasource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    private Long id;
    private String name;
    private String surname;
    private String city;
    private Long phone;
    private Double weight;
    private Double height;
    private Account account;
    private LocalDate birthday;

    public Client(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.name = resultSet.getString("name");
        this.surname = resultSet.getString("surname");
        this.city = resultSet.getString("city");
        this.phone = resultSet.getLong("phone");
        this.weight = resultSet.getDouble("weight");
        this.height = resultSet.getDouble("height");
        Datasource.getInstance()
                .getAccountDao()
                .get(resultSet.getLong("accountid"))
                .ifPresent(this::setAccount);
    }
}
