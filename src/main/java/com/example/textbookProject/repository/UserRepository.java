package com.example.textbookProject.repository;

import com.example.textbookProject.model.Book;
import com.example.textbookProject.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long>, JpaSpecificationExecutor<Book> {

    Optional<User> findByUsername(String username);
}
