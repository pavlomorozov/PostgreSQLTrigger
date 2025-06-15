package com.example;

import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.PGNotification;

public class PgListener {

    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/callback", "postgres", "postgres");
        PgConnection pgconn = conn.unwrap(PgConnection.class);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("LISTEN hero_update");
        }

        System.out.println("Enter \"exit\" to quit.");
        Thread listener = new Thread(() -> {
            try {
                while (true) {
                    PGNotification[] notifications = pgconn.getNotifications();
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            System.out.println("Update received: " + notification.getParameter());
                        }
                    }
                    /*
                    This technique more effective, because application listens to notifications,
                    instead of requesting data from database. But still, this is a polling, not a callback.
                     */
                    Thread.sleep(10000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listener.setDaemon(true);
        listener.start();

        //Prevents app shutdown until user enters "exit"
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!reader.readLine().trim().equalsIgnoreCase("exit")) {}
        conn.close();
    }
}