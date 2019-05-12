package ru.grape.course.server.resolver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import ru.grape.course.server.commons.DaoAction;
import ru.grape.course.server.dao.Datasource;
import ru.grape.course.server.dao.client.ClientDao;
import ru.grape.course.server.dao.service.ServiceDao;
import ru.grape.course.server.model.*;
import ru.grape.course.server.utils.CustomHash;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoResolver implements Resolver {
    private static DaoResolver instance;
    private final Datasource datasource;
    private final Gson gson;

    private DaoResolver() {
        gson = new Gson();
        datasource = Datasource.getInstance();
    }

    public static DaoResolver getInstance() {
        if (instance == null) {
            synchronized (DaoResolver.class) {
                if (instance == null) {
                    instance = new DaoResolver();
                }
            }
        }
        return instance;
    }

    @Override
    public void resolve(DaoAction daoAction, JSONObject inputObject, PrintWriter writer) throws SQLException {
        switch (daoAction) {
            case GIVE_ME_HASH:
                System.out.println("GIVE_ME_HASH");
                giveHash(inputObject, writer);
                break;
            case CHECK_IF_LOGIN_EXISTS:
                System.out.println("CHECK_IF_LOGIN_EXISTS");
                checkIfLoginExists(inputObject, writer);
                break;
            case GET_ALL_EXERCISES:
                System.out.println("GET_ALL_EXERCISES");
                getAllExercises(inputObject, writer);
                break;
            case GET_CLIENT_BY_ACCOUNT_ID:
                System.out.println("GET_CLIENT_BY_ACCOUNT_ID");
                getClientByAccountId(inputObject, writer);
                break;
            case UPDATE_CLIENT:
                System.out.println("UPDATE_CLIENT");
                updateClient(inputObject, writer);
                break;
            case ADD_CLIENT_SERVICE:
                System.out.println("ADD_CLIENT_SERVICE");
                addClientService(inputObject);
                break;
            case GET_SERVICES_BY_CLIENT_ID:
                System.out.println("GET_SERVICES_BY_CLIENT_ID");
                getServicesByCliendId(inputObject, writer);
                break;
            case DELETE_CLIENT_SERVICE_BY_EXERCISE_ID:
                System.out.println("DELETE_CLIENT_SERVICE_BY_EXERCISE_ID");
                deleteClientServiceByExerciseId(inputObject);
                break;
            case UPDATE_RATE_BY_CLIENT_ID:
                System.out.println("UPDATE_RATE_BY_CLIENT_ID");
                updateRateByClientId(inputObject);
                break;
            case GET_USERS:
                System.out.println("GET_USERS");
                getUsers(inputObject, writer);
                break;
            case GET_EXPERTS:
                System.out.println("GET_EXPERTS");
                getExperts(inputObject, writer);
                break;
            case DELETE_CLIENT_BY_LOGIN:
                System.out.println("DELETE_CLIENT_BY_LOGIN");
                deleteClientByLogin(inputObject);
                break;
            case ADD_CLIENT_HAVING_LOGIN_PASSWORD_ROLE:
                System.out.println("ADD_CLIENT_HAVING_LOGIN_PASSWORD_ROLE");
                addClientHavingLoginPasswordRole(inputObject);
                break;
            case GET_GOALS:
                System.out.println("GET_GOALS");
                getGoals(writer);
                break;
            case UPDATE_GOALS:
                System.out.println("UPDATE_GOALS");
                updateGoals(inputObject);
                break;
            case ADD_CLIENT:
                System.out.println("ADD_CLIENT");
                addClient(inputObject);
                break;
            case GET_RATES:
                System.out.println("GET_RATES");
                getRates(writer);
                break;
            case GET_ALL_CLIENTS:
                System.out.println("GET_ALL_CLIENTS");
                getAllClients(writer);
                break;
            case GET_ALL_SERVICES:
                System.out.println("GET_ALL_SERVICES");
                getAllServices(writer);
                break;
            case GET_RATE_BY_CLIENT_ID:
                System.out.println("GET_RATE_BY_CLIENT_ID");
                getRateByClientId(inputObject, writer);
                break;
        }

    }

    private void getRateByClientId(JSONObject inputObject, PrintWriter writer) throws SQLException {
        long id = inputObject.getLong("id");
        Rate rateByClientId = datasource.getRateDao().getRateByClientId(id);
        JSONObject outputJson = new JSONObject();
        outputJson.put("rate", gson.toJson(rateByClientId));
        writer.println(outputJson);
    }

    private void getAllServices(PrintWriter writer) throws SQLException {
        JSONObject outputJson = new JSONObject();
        outputJson.put("services", gson.toJson(datasource.getServiceDao().getAll()));
        writer.println(outputJson);
    }

    private void getAllClients(PrintWriter writer) throws SQLException {
        JSONObject outputJson = new JSONObject();
        outputJson.put("clients", gson.toJson(datasource.getClientDao().getAll()));
        writer.println(outputJson);
    }

    private void getRates(PrintWriter writer) throws SQLException {
        List<Rate> all = datasource.getRateDao().getAll();
        JSONObject outputJson = new JSONObject();
        outputJson.put("rates", gson.toJson(all));
        writer.println(outputJson);
    }

    private void addClient(JSONObject inputObject) throws SQLException {
        Client client = gson.fromJson(inputObject.getString("client"), Client.class);
        datasource.getClientDao().save(client);
    }

    private void updateGoals(JSONObject inputObject) {
        Type goalsList = new TypeToken<ArrayList<Goal>>() {
        }.getType();
        List<Goal> goals = gson.fromJson(inputObject.getString("goals"), goalsList);
        datasource.getGoalDao().updateGoals(goals);
    }

    private void getGoals(PrintWriter writer) throws SQLException {
        List<Goal> all = datasource.getGoalDao().getAll();
        JSONObject outputJson = new JSONObject();
        outputJson.put("goals", gson.toJson(all));
        writer.println(outputJson);
    }

    private void addClientHavingLoginPasswordRole(JSONObject inputObject) throws SQLException {
        Account account = gson.fromJson(inputObject.getString("account"), Account.class);
        datasource.getClientDao().addClientByAccount(account);
    }

    private void deleteClientByLogin(JSONObject inputObject) throws SQLException {
        String login = inputObject.getString("login");
        datasource.getClientDao().deleteClientByLogin(login);
    }

    private void getExperts(JSONObject inputObject, PrintWriter writer) throws SQLException {
        Role role = gson.fromJson(inputObject.getString("role"), Role.class);
        List<Account> byRole = datasource.getAccountDao().getByRole(role);
        JSONObject outputJson = new JSONObject();
        outputJson.put("experts", gson.toJson(byRole));
        writer.println(outputJson);
    }

    private void getUsers(JSONObject inputObject, PrintWriter writer) throws SQLException {
        Role role = gson.fromJson(inputObject.getString("role"), Role.class);
        List<Account> byRole = datasource.getAccountDao().getByRole(role);
        JSONObject outputJson = new JSONObject();
        outputJson.put("users", gson.toJson(byRole));
        writer.println(outputJson);
    }

    private void updateRateByClientId(JSONObject inputObject) throws SQLException {
        Rate rate = gson.fromJson(inputObject.getString("rate"), Rate.class);
        datasource.getRateDao().update(rate);
    }

    private void deleteClientServiceByExerciseId(JSONObject inputObject) throws SQLException {
        Long client_id = inputObject.getLong("client_id");
        Integer exercise_id = inputObject.getInt("exercise_id");
        ServiceDao serviceDao = datasource.getServiceDao();
        serviceDao.deleteClientServiceByExerciseId(client_id, exercise_id);
    }

    private void getServicesByCliendId(JSONObject inputObject, PrintWriter writer) throws SQLException {
        long clientId = inputObject.getLong("id");
        List<Service> byClientId = datasource.getServiceDao().getByClientId(clientId);
        JSONObject outputJson = new JSONObject();
        outputJson.put("services", gson.toJson(byClientId));
        writer.println(outputJson);
    }

    private void addClientService(JSONObject inputObject) throws SQLException {
        Service service = gson.fromJson(inputObject.getString("service"), Service.class);
        datasource.getServiceDao().save(service);
    }

    private void updateClient(JSONObject inputObject, PrintWriter writer) throws SQLException {
        ClientDao clientDao = datasource.getClientDao();
        clientDao.update(gson.fromJson(inputObject.getString("client"), Client.class));
    }

    private void getClientByAccountId(JSONObject inputObject, PrintWriter writer) throws SQLException {
        long accountId = inputObject.getLong("id");
        Client clientByAccountId = datasource.getClientDao().getClientByAccountId(accountId);
        JSONObject outputJson = new JSONObject();
        outputJson.put("client", gson.toJson(clientByAccountId));
        writer.println(outputJson);
    }

    private void getAllExercises(JSONObject inputObject, PrintWriter writer) throws SQLException {
        List<Exercise> exerciseList = datasource.getExerciseDao().getAll();
        JSONObject outputObject = new JSONObject();
        outputObject.put("exercises", gson.toJson(exerciseList));
        writer.println(outputObject);
    }

    private void checkIfLoginExists(JSONObject inputObject, PrintWriter writer) throws SQLException {
        String login = inputObject.getString("login");
        Account account = datasource.getAccountDao().getByLogin(login);
        JSONObject object = new JSONObject();
        object.put("account", gson.toJson(account));
        writer.println(object);
    }

    private void giveHash(JSONObject inputObject, PrintWriter writer) {
        String toHash = inputObject.getString("toHash");
        String hash = CustomHash.getHash(toHash, 32);
        JSONObject object = new JSONObject();
        object.put("hashed", hash);
        writer.println(object);
    }


    public Gson getGson() {
        return gson;
    }
}
