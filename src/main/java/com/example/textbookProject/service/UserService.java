package com.example.textbookProject.service;

import com.example.textbookProject.model.Book;
import com.example.textbookProject.model.User;
import com.example.textbookProject.repository.BookRepository;
import com.example.textbookProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository)
    {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }
    public Iterable<User> searchAll(){
        return userRepository.findAll();
    }

    public boolean addUser(User user){
        try {
            this.userRepository.save(user);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void decreaseCredits(String username){

        Optional<User> userOptional = userRepository.findByUsername(username);

        if(!userOptional.isPresent())
        {
            System.out.println("Unable to find user to decrease credits");
            return;
        }

        User user = userOptional.get();

        user.setCredits(user.getCredits() - 1);
        userRepository.save(user);

        System.out.println("User credits updated and saved");
    }

    public void increaseCredits(String username){

        Optional<User> userOptional = userRepository.findByUsername(username);

        if(!userOptional.isPresent())
        {
            System.out.println("Unable to find user to increase credits");
            return;
        }


        User user = userOptional.get();
        user.setCredits(user.getCredits() + 1);
        userRepository.save(user);
        System.out.println("User credits updated and saved");
    }

    public Optional<User> findByUsername(String username){

        return userRepository.findByUsername(username);
    }
}
