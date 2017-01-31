package com.example.app.bale.data;

import android.os.AsyncTask;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by 13108306 on 17/10/2016.
 */
public class PostgresDatabase implements Runnable {

    private Connection connection;
    private String host;
    private String db;
    private int port = 5432;
    private String user;
    private String password;
    // Formato: postgres://<username>:<password>@<host>:<port>/<dbname>
    private String DATABASE_URL = "postgres://%s:%s@%s:%d/%s";
    // Formato: jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>
    private String JDBC_DATABASE_URL = "jdbc:postgresql://%s:%d/%s?sslmode=require&user=%s&password=%s";

    public PostgresDatabase() {
        connection = null;
        host = "ec2-54-243-218-37.compute-1.amazonaws.com";
        db = "dba5q9b5vbjnsd";
        port = 5432;
        user = "wzyxqcanyrmhox";
        password = "WfisA0ycdPiZk7sUtMKo7LoU0U";
        DATABASE_URL = String.format(this.DATABASE_URL, this.user, this.password, this.host,
                this.port, this.db);
        JDBC_DATABASE_URL = String.format(this.JDBC_DATABASE_URL, this.host, this.port, this.db,
                this.user, this.password);
        this.conecta();
    }

    @Override
    public void run() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(this.JDBC_DATABASE_URL, this.user, this.password);
            /*
            * String dbUrl = System.getenv("JDBC_DATABASE_URL");
              return DriverManager.getConnection(dbUrl);
            * */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }

    private void conecta() {
        Thread thread = new Thread(this);
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void desconecta() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.connection = null;
            }
        }
    }

    public ResultSet executa(String query) {
        this.conecta();
        ResultSet resultSet = null;
        try {
            resultSet = new ExecuteDBTask(this.connection, query).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    private class ExecuteDBTask extends AsyncTask<String, Void, ResultSet> {

        private Connection connection;
        private String query;

        public ExecuteDBTask(Connection connection, String query) {
            this.connection = connection;
            this.query = query;
        }

        @Override
        protected ResultSet doInBackground(String... params) {
            ResultSet resultSet = null;
            try {
                resultSet = connection.prepareStatement(query).executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultSet;
        }
    }


}
