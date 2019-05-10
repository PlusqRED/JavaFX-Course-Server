package ru.grape.course.server.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private Long id;
    private String login;
    private String password;
    private Role role;

    public Account(ResultSet accountSet) throws SQLException {
        this.id = accountSet.getLong("id");
        this.login = accountSet.getString("login");
        this.password = accountSet.getString("password");
        this.role = Role.identify(accountSet.getInt("role_id"));
    }
}
