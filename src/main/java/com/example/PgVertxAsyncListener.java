package com.example;

import io.vertx.core.*;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PgVertxAsyncListener {

    private static final Vertx vertx = Vertx.vertx();

    public static void main(String[] args) throws Exception {

        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("callback")
                .setUser("postgres")
                .setPassword("postgres");

        PgConnection
                .connect(vertx, connectOptions)
                .onComplete(res -> {
                    if (res.succeeded()) {
                        System.out.println("Connected");
                        listen(res.result());
                    } else {
                        System.out.println("Could not connect " + res.cause());
                    }
                });

        //Prevents app shutdown until user enters "exit"
        System.out.println("Enter \"exit\" to quit.");
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            while (!reader.readLine().trim().equalsIgnoreCase("exit")) {
                // wait for user input to exit
            }
            //close Vertx
            vertx.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

    public static void listen(PgConnection connection) {
        connection.notificationHandler(notification ->
            System.out.println("Received " + notification.getPayload() + " on channel " + notification.getChannel()));
        connection
                .query("LISTEN hero_update")
                .execute()
                .onComplete(ar -> System.out.println("Subscribed to channel"));
    }
}
