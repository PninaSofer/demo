package com.example.demo.services;

import com.example.demo.objects.MessageObject;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MessageService {

    private Connection connection;

    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ashCollege", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean addMessage (String token, String receiver, String title, String content, String sendDate) {
        boolean success = false;

        try {
            Integer receiverId = getUserIdByUsername(receiver);
            Integer senderId = getUserIdByToken(token); // user sending the message
            if (senderId != null && receiverId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO messages (senderId, receiverId, title, content, sendDate) VALUE (?, ?, ?, ?, NOW())");
                preparedStatement.setInt(1, senderId);
                preparedStatement.setInt(2, receiverId);
                preparedStatement.setString(3, title);
                preparedStatement.setString(4, content);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public Integer getUserIdByUsername (String username) {
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT id FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Integer getUserIdByToken (String token) {
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT id FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public List<MessageObject> getMessagesByUser (String token) {
        List<MessageObject> messageObjects = new ArrayList<>();
        String sql = "select m.id, s.username as sender, r.username as receiver, m.title, m.content, m.sendDate, m.readDate" +
                " from messages m" +
                " inner join users s on s.id = m.senderId" +
                " inner join users r on r.id = m.receiverId" +
                " WHERE receiverId = ? ORDER BY m.sendDate DESC";
        try {
            Integer userId = getUserIdByToken(token);
            if (userId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {

                    String sender = resultSet.getString("sender");
                    String receiver = resultSet.getString("receiver");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    String sendDate = resultSet.getString("sendDate");
                    String readDate = resultSet.getString("readDate");
                    int messageId =  resultSet.getInt("id");

                    MessageObject messageObject = new MessageObject(messageId, sender, receiver, title, content, sendDate, readDate);

//                    messageObject.setDate(date);
                    messageObjects.add(messageObject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageObjects;
    }

    public boolean markAsRead(int messageId){
        boolean success = false;
        try{
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "update messages set readDate = ? WHERE id = ?");
            preparedStatement.setString(1, LocalDateTime.now().toString());
            preparedStatement.setInt(2, messageId);
            preparedStatement.executeUpdate();
            success = true;
        }catch(SQLException e){
            e.printStackTrace();
            return success;
        }
        return success;
    }

    public boolean removeMessage (String token, int messageId) {
        boolean success = false;
        try {
            Integer userId = getUserIdByToken(token);
            if (userId != null) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM messages WHERE id = ? AND receiverId = ? ");
                preparedStatement.setInt(1, messageId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

}
