package com.example.demo.controllers;

import java.util.List;
import com.example.demo.objects.MessageObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.services.UserService;
import com.example.demo.services.MessageService;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public List<MessageObject> getMessage (String token) {

        List<MessageObject>  messages = messageService.getMessagesByUser(token);
        return messages;
    }

    @RequestMapping("/add")
    public boolean createMessage (String token, String receiver, String title, String content, String sendDate){
        boolean success = false;

        success = messageService.addMessage(token, receiver, title, content, sendDate);
        return success;
    }

    @RequestMapping("/mark")
    public boolean markAsRead(Integer messageId){
        boolean success = false;
        success = messageService.markAsRead(messageId);
        return success;
    }


    @RequestMapping("remove-message")
    public boolean removePost (String token, int messageId) {
        return messageService.removeMessage(token, messageId);
    }

    @RequestMapping("sign-in")
    public String signIn (String username, String password) {
        String token = userService.getTokenByUsernameAndPassword(username, password);
        return token;
    }
}