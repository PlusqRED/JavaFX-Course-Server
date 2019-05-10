package ru.grape.course.server.dao.goal;

import ru.grape.course.server.dao.CrudDao;
import ru.grape.course.server.model.Goal;

import java.util.List;

public interface GoalDao extends CrudDao<Goal> {
    void updateGoals(List<Goal> goals);
}
