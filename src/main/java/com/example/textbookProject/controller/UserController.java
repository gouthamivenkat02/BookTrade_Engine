package com.example.textbookProject.controller;

import com.example.textbookProject.model.Book;
import com.example.textbookProject.model.User;
import com.example.textbookProject.service.BookService;
import com.example.textbookProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/all")
    public Iterable<User> getAllUsers(){
        return userService.searchAll();
    }

    @PostMapping("/add")
    public boolean addUser(@RequestBody User user){

        System.out.println("<debug> User " + user.toString());
        return userService.addUser(user);
    }
}
