package com.whatsapp.controller;

import com.whatsapp.entity.User;
import com.whatsapp.exception.UserException;
import com.whatsapp.request.UpdateUserRequest;
import com.whatsapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUserHandler(@RequestBody UpdateUserRequest req, @PathVariable Integer userId) throws UserException {
        System.out.println("updated user");
        User updatedUser=userService.updateUser(userId,req);
        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt ) throws UserException {

        User user=userService.findUserProfile(jwt);
        return new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<User>> searchUser(@PathVariable String keyword) {
        List<User> user = userService.searchUser(keyword);
        return new ResponseEntity<List<User>>(user, HttpStatus.ACCEPTED);
    }

}
