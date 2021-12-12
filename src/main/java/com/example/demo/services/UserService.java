package com.example.demo.services;

import com.example.demo.objects.UserObject;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.sql.*;


@Component
public class UserService {
    private static final String USER_BLOCKED = "USER_BLOCKED";
    private static final String INVALID_USER = "INVALID_USER";
    private static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    private static final String SERVER_ERROR = "SERVER_ERROR";

    private Connection connection; //connect to DB

    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ashCollege", "root", "1234");
        } catch (SQLException e) { //when we have problem
            e.printStackTrace();
        }
    }

    public boolean userExists(String username){
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public String getTokenByUsernameAndPassword(String username, String password) {
        String token = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT token, password, numberOfRetries FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String dbPassword = resultSet.getString("password");
                if(!dbPassword.equals((password))){
                    int numberOfRetries = resultSet.getInt("numberOfRetries");
                    if(numberOfRetries < 5){
                        incNumberOfRetries(username, numberOfRetries);
                        return INVALID_PASSWORD;
                    }else{
                        blockUser(username);
                        return USER_BLOCKED;
                    }

                }
                token = resultSet.getString("token");
            }else{
                return INVALID_USER;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return SERVER_ERROR;
        }
        return token;
    }

    private void incNumberOfRetries(String username, int numberOfRetries){
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "update users set numberOfretries = ? WHERE username = ?");
            preparedStatement.setInt(1, numberOfRetries+1);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void blockUser(String username){
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "update users set isblocked = ? WHERE username = ?");
            preparedStatement.setInt(1, 1);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean addUser (UserObject userObject) {
        boolean success = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO users (username, password, token, isblocked, numberOfRetries) VALUE (?, ?, ?, ? ,?)");
            preparedStatement.setString(1, userObject.getUsername());
            preparedStatement.setString(2, userObject.getPassword());
            preparedStatement.setString(3, userObject.getToken());
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.executeUpdate();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
        }
        return success;

    }
}
