package com.example.textbookProject.repository;

import com.example.textbookProject.model.Book;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book,Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByIsbn(String isbn);

    Optional<List<Book>> findByUsername(String username);
}
