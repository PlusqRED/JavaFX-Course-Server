package ru.grape.course.server.dao;

import ru.grape.course.server.dao.account.AccountDao;
import ru.grape.course.server.dao.account.AccountDaoImpl;
import ru.grape.course.server.dao.client.ClientDao;
import ru.grape.course.server.dao.client.ClientDaoImpl;
import ru.grape.course.server.dao.exercise.ExerciseDao;
import ru.grape.course.server.dao.exercise.ExerciseDaoImpl;
import ru.grape.course.server.dao.goal.GoalDao;
import ru.grape.course.server.dao.goal.GoalDaoImpl;
import ru.grape.course.server.dao.rate.RateDao;
import ru.grape.course.server.dao.rate.RateDaoImpl;
import ru.grape.course.server.dao.service.ServiceDao;
import ru.grape.course.server.dao.service.ServiceDaoImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class Datasource {
    private static Datasource instance;
    private Connection connection;

    public static Datasource getInstance() {
        if (instance == null) {
            synchronized (Datasource.class) {
                if (instance == null) {
                    instance = new Datasource();
                }
            }
        }
        return instance;
    }

    private Datasource() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(getClass().getResource("/properties/db.properties").toURI())));
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
        } catch (IOException | ClassNotFoundException | SQLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public AccountDao getAccountDao() {
        return AccountDaoImpl.getInstance();
    }

    public ExerciseDao getExerciseDao() {
        return ExerciseDaoImpl.getInstance();
    }
    public ClientDao getClientDao() {
        return ClientDaoImpl.getInstance();
    }
    public ServiceDao getServiceDao() {
        return ServiceDaoImpl.getInstance();
    }

    public RateDao getRateDao() {
        return RateDaoImpl.getInstance();
    }
    public GoalDao getGoalDao() {
        return GoalDaoImpl.getInstance();
    }
}
