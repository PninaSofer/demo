package com.example.demo.controllers;


import com.example.demo.objects.UserObject;
import com.example.demo.services.UserService;
import com.example.demo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public boolean createUser (String username, String password){
        boolean success = false;
        boolean response = userService.userExists(username);
        if(response == true){
            return false;
        }
        UserObject userObject = new UserObject();
        userObject.setUsername(username);
        userObject.setPassword(password);
        String hash = Utils.createHash(username, password);
        userObject.setToken(hash);
        success = userService.addUser(userObject);
        return success;
    }


    @RequestMapping("sign-in")
    public String signIn (String username, String password) {

        String token = userService.getTokenByUsernameAndPassword(username, password);

        return token;
    }

    @RequestMapping("validate")
    public boolean validateUser (String username) {
        boolean userExists = userService.userExists(username);
        return userExists;
    }
}