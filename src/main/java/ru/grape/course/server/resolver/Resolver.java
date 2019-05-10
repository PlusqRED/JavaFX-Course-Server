package ru.grape.course.server.resolver;

import org.json.JSONObject;
import ru.grape.course.server.commons.DaoAction;

import java.io.PrintWriter;
import java.sql.SQLException;

public interface Resolver {
    void resolve(DaoAction daoAction, JSONObject inputObject, PrintWriter writer) throws SQLException;
}
