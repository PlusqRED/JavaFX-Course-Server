package ru.grape.course.server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.json.JSONObject;
import ru.grape.course.server.commons.DaoAction;
import ru.grape.course.server.resolver.DaoResolver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Properties;

public class App extends Application{
    private DaoResolver daoResolver = DaoResolver.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        startServer();
    }

    private void startServer() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(getClass().getResource("/properties/server.properties").toURI())));
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(properties.getProperty("server.port")));
            while (true) {
                System.out.println("Server ready to accept user...");
                acceptUser(serverSocket.accept());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void acceptUser(Socket user) {
        System.out.println("User accepted...");
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(user.getInputStream()));
                 PrintWriter writer = new PrintWriter(user.getOutputStream(), true)) {
                while (!user.isClosed()) {
                    JSONObject jsonObject = new JSONObject(reader.readLine());
                    DaoAction daoAction = daoResolver
                            .getGson()
                            .fromJson(jsonObject.getString("action"), DaoAction.class);
                    daoResolver.resolve(daoAction, jsonObject, writer);
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                System.out.println("Client has been disconnected!");
            }
        }).start();
    }
}