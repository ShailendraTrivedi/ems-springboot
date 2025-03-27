package com.system.event_management.controller;

import com.system.event_management.exception.UserException;
import com.system.event_management.model.userbeans.login.LoginRequestBean;
import com.system.event_management.model.userbeans.login.LoginResponseBean;
import com.system.event_management.model.userbeans.user.UserRequestBean;
import com.system.event_management.model.userbeans.user.UserResponseBean;
import com.system.event_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseBean<?>> createUser(@RequestBody UserRequestBean userRequestBean) throws UserException {
        return new ResponseEntity<>(this.userService.createUser(userRequestBean), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseBean<?>> login(@RequestBody LoginRequestBean loginRequestBean) {
        return new ResponseEntity<>(this.userService.loginUser(loginRequestBean),HttpStatus.OK);
    }




}
