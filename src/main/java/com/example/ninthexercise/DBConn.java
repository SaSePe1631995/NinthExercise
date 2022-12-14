package com.example.ninthexercise;
import java.sql.*;

public class DBConn {

    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DBNAME = "9ex";
    private final String USERNAME = "root";
    private final String USERPASS = "root";
    private Connection dbConn = null;

    private Connection getDBConnection () throws ClassNotFoundException, SQLException {
        String connStr = "jdbc:mysql://"+HOST+":"+PORT+"/"+DBNAME;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConn = DriverManager.getConnection(connStr,USERNAME,USERPASS);
        return dbConn;
    }

    public void isConnected () throws ClassNotFoundException, SQLException {
        dbConn = getDBConnection();
        System.out.println(dbConn.isValid(2000));
    }

    public boolean isExistUser (String login) {

        String query = "SELECT `id` FROM `users` WHERE `login` LIKE ?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isExistEmail (String email) {

        String query = "SELECT `id` FROM `users` WHERE `email` LIKE ?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean passwordCheck (String login, String password) {

        String query = "SELECT `id` FROM `users` WHERE `login` LIKE ? AND `password` LIKE ?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(query);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String fillEmail (String login, String email){

        String query = "SELECT `email` FROM `users` WHERE `login` LIKE ?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(query);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("email");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return email;
    }

    public void addData(String loginOLD, String emailOLD, String login, String email) {

        String query = "UPDATE `users` SET `login` = ?, `email` = ? WHERE login LIKE ? AND email LIKE ?";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(query);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, loginOLD);
            preparedStatement.setString(4, emailOLD);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addData(String login, String email, String password) {

        String query = "INSERT INTO `users` (`id`, `login`, `email`, `password`) VALUES (NULL, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = getDBConnection().prepareStatement(query);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
