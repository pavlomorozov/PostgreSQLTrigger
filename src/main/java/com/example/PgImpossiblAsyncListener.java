package com.example;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Statement;

public class PgImpossiblAsyncListener {

    public static void main(String[] args) throws Exception {

        //The org.postgresql.jdbc cannot operate in async way. Using the com.impossibl.postgres.api
        PGDataSource dataSource = new PGDataSource();
        dataSource.setHost("localhost");
        dataSource.setPort(5432);
        dataSource.setDatabaseName("callback");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");

        PGNotificationListener listener = new PGNotificationListener(){
            @Override
            public void notification(int processId, String channelName, String payload) {
                System.out.println("notification = " + channelName + ", " + payload);
            }
        };

        try (PGConnection connection = (PGConnection) dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("LISTEN hero_update");
            statement.close();
            connection.addNotificationListener(listener);
        } catch (Exception e) {
            System.err.println(e);
        }

        //Prevents app shutdown until user enters "exit"
        System.out.println("Enter \"exit\" to quit.");
        try(InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(inputStreamReader)){
            while (!reader.readLine().trim().equalsIgnoreCase("exit")) {
                // wait for user input to exit
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
