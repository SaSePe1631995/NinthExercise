package com.example.ninthexercise.controllers;

import com.example.ninthexercise.DBConn;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainController {

    @FXML
    private Button changeButton;

    @FXML
    private TextField emailWindow;

    @FXML
    private TextField loginWindow;

    @FXML
    private PasswordField passwordWindow;

    @FXML
    private Label infoLabel;

    @FXML
    private CheckBox editChecker;

    private final DBConn dbConn = new DBConn();
    private boolean checker = false;
    private String LFC = "", EFC = "";

    private void registerUser() {

        passwordWindow.setStyle("-fx-border-color: #ffffff");
        loginWindow.setStyle("-fx-border-color: #ffffff");
        emailWindow.setStyle("-fx-border-color: #ffffff");
        infoLabel.setText("");

        String login = loginWindow.getText();
        String email = emailWindow.getText();
        String password = passwordWindow.getText();

        if (dbConn.isExistUser(login)) {

            infoLabel.setText("Пользователь с таким логином уже существует!");
            loginWindow.setStyle("-fx-border-color: #ff0000");

        }
        else if (login.length() < 5){

            infoLabel.setText("Задан некорректный логин!");
            loginWindow.setStyle("-fx-border-color: #ff0000");

        }
        else if (dbConn.isExistEmail(email)){

            infoLabel.setText("Указанный адрес занят другим пользователем!");
            emailWindow.setStyle("-fx-border-color: #ff0000");

        }
        else if (!email.contains("@") || !email.contains(".") || email.length() < 5){

            infoLabel.setText("Задан некорректный адрес электронной почты!");
            emailWindow.setStyle("-fx-border-color: #ff0000");

        }
        else if (password.length() < 3){

            infoLabel.setText("Задан некорректный пароль!");
            passwordWindow.setStyle("-fx-border-color: #ff0000");

        }
        else{

            dbConn.addData(login,email,cashPassword(password));
            passwordWindow.setText("");
            loginWindow.setText("");
            emailWindow.setText("");
            infoLabel.setText("Новый пользователь успешно зарегистрирован");

        }

    }

    private void authorisationUser() {

        passwordWindow.setStyle("-fx-border-color: #ffffff");
        loginWindow.setStyle("-fx-border-color: #ffffff");
        emailWindow.setStyle("-fx-border-color: #ffffff");
        infoLabel.setText("");

        String login = loginWindow.getText();
        String password = passwordWindow.getText();

        if (!dbConn.isExistUser(login)) {

            infoLabel.setText("Пользователь с таким логином не существует!");
            loginWindow.setStyle("-fx-border-color: #ff0000");

        }
        else if (!dbConn.passwordCheck(login, cashPassword(password))){

            infoLabel.setText("Введён некорректный пароль!");
            passwordWindow.setStyle("-fx-border-color: #ff0000");

        }
        else{

            passwordWindow.setText("");
            loginWindow.setText("");
            emailWindow.setText("");
            infoLabel.setText("Авторизация успешно произведена!");

        }

    }

    private void editUser() {

        passwordWindow.setStyle("-fx-border-color: #ffffff");
        loginWindow.setStyle("-fx-border-color: #ffffff");
        emailWindow.setStyle("-fx-border-color: #ffffff");
        infoLabel.setText("");

        String login = loginWindow.getText();
        String email = emailWindow.getText();
        String password = passwordWindow.getText();

        if (!dbConn.passwordCheck(LFC, cashPassword(password))){

            infoLabel.setText("Введён некорректный пароль!");
            passwordWindow.setStyle("-fx-border-color: #ff0000");

        }
        else{

            dbConn.addData(LFC, EFC, login, email);
            passwordWindow.setText("");
            loginWindow.setText("");
            emailWindow.setText("");
            infoLabel.setText("Данные обновлены!");

        }
    }

    private void checkEdit(){

        if (!checker){

            passwordWindow.setStyle("-fx-border-color: #ffffff");
            loginWindow.setStyle("-fx-border-color: #ffffff");
            emailWindow.setStyle("-fx-border-color: #ffffff");

            String login = loginWindow.getText();
            String email = emailWindow.getText();

            if (dbConn.isExistUser(login) && dbConn.isExistEmail(email)) editChecker.setDisable(false);
            else editChecker.setDisable(true);

            if (dbConn.isExistUser(login)) changeButton.setText("Авторизоваться");
            else changeButton.setText("Зарегистрироваться");
        }
    }

    private void editTrue(){

        if (editChecker.isSelected()) {
            checker = true;
            LFC = loginWindow.getText();
            EFC = emailWindow.getText();
            changeButton.setText("Обновиться");
        }
        else{
            checker = false;
            checkEdit();
        }

    }

    private void fillEmail(){

        if (dbConn.isExistUser(loginWindow.getText())) {
            emailWindow.setText(dbConn.fillEmail(loginWindow.getText(), emailWindow.getText()));
            checkEdit();
        }


    }

    private String cashPassword (String password){

        MessageDigest messageDigest;
        byte [] digest;

        try{

            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes());
            digest = messageDigest.digest();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        BigInteger bigInteger = new BigInteger(1, digest);
        StringBuilder MD5Hexed = new StringBuilder(bigInteger.toString(16));

        while (MD5Hexed.length() < 32) MD5Hexed.append("0");

        return MD5Hexed.toString();

    }

    public void initialize(){

        checkEdit();
        fillEmail();

        loginWindow.setOnKeyTyped(event->{
            checkEdit();
            fillEmail();
        });

        emailWindow.setOnKeyTyped(event-> checkEdit());

        passwordWindow.setOnKeyTyped(event-> checkEdit());

        editChecker.setOnAction(event-> editTrue());

        changeButton.setOnAction(event->{
            if (changeButton.getText().equals("Зарегистрироваться")) registerUser();
            if (changeButton.getText().equals("Авторизоваться")) authorisationUser();
            if (changeButton.getText().equals("Обновиться")) editUser();
        });

    }
}
