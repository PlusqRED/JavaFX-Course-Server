package ru.grape.course.server.dao.rate;

import ru.grape.course.server.dao.CrudDao;
import ru.grape.course.server.model.Rate;

import java.sql.SQLException;

public interface RateDao extends CrudDao<Rate> {
    Rate getRateByClientId(long id) throws SQLException;
}
