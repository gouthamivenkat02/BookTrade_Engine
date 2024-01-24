package com.example.textbookProject.service;

import com.example.textbookProject.model.Book;
import com.example.textbookProject.model.User;
import com.example.textbookProject.repository.BookRepository;
import com.example.textbookProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

@Service
public class BookService {


    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository)
    {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }
    public Iterable<Book> searchAll(){
        return bookRepository.findAll();
    }

    public boolean addBook(Book book){
        try {
            this.bookRepository.save(book);
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean canBuy(Long id, String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()) return false;
        if(user.get().credits <= 0)
            return false;
        return true;
    }

    public boolean addBooks(List<Book> books) {
        try {
            bookRepository.saveAll(books);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> searchBooks(String isbn, String title, String author, String edition, Integer price) {
        return bookRepository.findAll((Specification<Book>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // ISBN
            if (isbn != null && !isbn.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), isbn));
            }

            // Title
            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            // Author
            if (author != null && !author.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
            }

            // Edition
            if (edition != null && !edition.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("edition"), edition));
            }

            // Price
            if (price != null) {
                predicates.add(criteriaBuilder.equal(root.get("price"), price));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    // Buy a book
    public Optional<Book> buyBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()) {
            Book existingBook = book.get();

            // Check if the book is already marked as SOLD
            if(existingBook.getStatus() == Book.BookStatus.SOLD) {
                return Optional.empty(); // Return an empty Optional to indicate the book cannot be bought
            }

            existingBook.setStatus(Book.BookStatus.SOLD);  // Mark the book as sold
            bookRepository.save(existingBook);
        }
        return book;
    }

    // Sell a book (existing book)
    public ResponseEntity<?> sellBookById(Long id,String username) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book existingBook = book.get();

            //check if the book is owned by the given user
            if(!existingBook.getUsername().equals(username))
                return ResponseEntity.badRequest().body("This user does not own the book");

            if (existingBook.getStatus() == Book.BookStatus.AVAILABLE) {
                return ResponseEntity.badRequest().body("Book already available");
            } else {
                // Calculate new price
                int previousPrice = existingBook.getPrice();
                depreciatePrice(existingBook);
                String message = String.format(
                        "Book successfully sold! Previous price: %d\n New price: %d\n\n Book details: %s\n",
                        previousPrice, existingBook.getPrice(), existingBook.toString()
                );
                return ResponseEntity.ok(message);
            }
        }
        return ResponseEntity.notFound().build();
    }


    public ResponseEntity<?> sellBookByISBN(String isbn, String title, String author, String edition, Integer price) {
        Optional<Book> existingBookOpt = bookRepository.findByIsbn(isbn);

        if (existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            if (existingBook.getStatus() == Book.BookStatus.SOLD) {
                int previousPrice = existingBook.getPrice();
                depreciatePrice(existingBook);
                String message = String.format(
                        "Book successfully sold back! Previous price: %d, New price: %d. Book details: %s",
                        previousPrice, existingBook.getPrice(), existingBook.toString()
                );
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.badRequest().body("Book is already available and cannot be sold back.");
            }
        } else {
            title = (title != null) ? title : "Unknown Title";
            author = (author != null) ? author : "Unknown Author";
            edition = (edition != null) ? edition : "Unknown Edition";
            int defaultPrice = (price != null && price > 0) ? price : 60; // Default price if not provided

            // Create a new book with the provided details or default values
            Book newBook = new Book.Builder()
                    .withIsbn(isbn)
                    .withTitle(title)
                    .withAuthor(author)
                    .withEdition(edition)
                    .withPrice(defaultPrice)
                    .build();

            bookRepository.save(newBook);
            return ResponseEntity.ok("New book added to inventory: " + newBook.toString());
        }
    }

    public void saveBook(Book book){
        bookRepository.save(book);
    }

    public void depreciatePrice(Book existingBook){
        int previousPrice = existingBook.getPrice();
        int newPrice = (int)(previousPrice * 0.9); // Depreciating by 10%
        existingBook.setPrice(newPrice);
        existingBook.setUsername("NULL"); //set username to NULL to indicate no user owning it
        existingBook.setStatus(Book.BookStatus.AVAILABLE);  // Mark the book as available
        bookRepository.save(existingBook);
    }


}















