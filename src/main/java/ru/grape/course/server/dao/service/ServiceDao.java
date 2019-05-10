package ru.grape.course.server.dao.service;

import ru.grape.course.server.dao.CrudDao;
import ru.grape.course.server.model.Service;

import java.sql.SQLException;
import java.util.List;

public interface ServiceDao extends CrudDao<Service> {
    List<Service> getByClientId(long clientId) throws SQLException;

    void deleteClientServiceByExerciseId(Long client_id, Integer exercise_id) throws SQLException;
}
