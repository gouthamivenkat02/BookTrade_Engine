package com.example.textbookProject.controller;

import com.example.textbookProject.model.Book;
import com.example.textbookProject.service.BookService;
import com.example.textbookProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/book")
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BookController(BookService bookService, UserService userService){
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public Iterable<Book> getAllBooks(){
        return bookService.searchAll();
    }

    @PostMapping("/add")
    public boolean addBook(@RequestBody Book book){

        System.out.println("<debug> Book received is " + book.toString());
        return bookService.addBook(book);
    }

    @PostMapping("/addBooks")
    public ResponseEntity<?> addBooks(@RequestBody List<Book> books) {
        boolean success = bookService.addBooks(books);
        if (success) {
            return ResponseEntity.ok("Books added successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to add books.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String edition,
            @RequestParam(required = false) Integer price
    ) {
        List<Book> books = bookService.searchBooks(isbn, title, author, edition, price);
        return ResponseEntity.ok(books);
    }


    @PostMapping("/buy/{username}/{id}")
    public ResponseEntity<?> buyBook(@PathVariable Long id, @PathVariable String username) {

        if(!bookService.canBuy(id,username))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist or has reached maximum limit for buying books");
        //decrease credits for the owner of the book
        userService.decreaseCredits(username);
        Optional<Book> boughtBookOpt = bookService.buyBook(id);
        if (boughtBookOpt.isPresent()) {
            Book boughtBook = boughtBookOpt.get();
            boughtBook.setUsername(username);
            bookService.saveBook(boughtBook);
            String message = String.format("Book bought successfully!\n\n Book details: %s", boughtBook.toString());
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book is already sold or does not exist.");
        }
    }


    // Sell a book by ID
    @PostMapping("/sellExisting/{username}/{id}")
    public ResponseEntity<?> sellBookById(@PathVariable Long id,@PathVariable String username) {
        ResponseEntity<?> response = bookService.sellBookById(id,username);
        userService.increaseCredits(username);

        return response;
    }


    @PostMapping("/sell")
    public ResponseEntity<?> sellBookByISBN(
            @RequestBody Book book) {
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("ISBN is mandatory.");
        }
        return bookService.sellBookByISBN(book.getIsbn(), book.getTitle(), book.getAuthor(), book.getEdition(), book.getPrice());
    }



}
