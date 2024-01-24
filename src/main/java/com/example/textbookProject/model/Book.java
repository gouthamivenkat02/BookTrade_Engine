package com.example.textbookProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonProperty("isbn")
    private String isbn;
    @JsonProperty("title")
    private String title;
    @JsonProperty("author")
    private String author;
    @JsonProperty("edition")
    private String edition;
    @JsonProperty("price")
    private int price;

    @JsonProperty("username")
    private String username = "None";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public enum BookStatus {
        AVAILABLE, SOLD
    }

    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;


    public Book() {
        // Private constructor to prevent direct instantiation
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", edition='" + edition + '\'' +
                ", price=" + price +
                ", status=" + status +
                '}';
    }

    public Book(long id, String isbn, String title, String author, String edition, int price, BookStatus status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.price = price;
        this.status = status;
    }

    public Book(int id, String isbn, String title, String author, String edition, int price) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.edition = edition;
        this.price = price;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }


    public static class Builder {
        private long id;
        private String isbn;
        private String title;
        private String author;
        private String edition;
        private int price;
        private BookStatus status = BookStatus.AVAILABLE;

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withEdition(String edition) {
            this.edition = edition;
            return this;
        }

        public Builder withPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder withStatus(BookStatus status) {
            this.status = status;
            return this;
        }

        public Book build() {
            Book book = new Book();
            book.id = this.id;
            book.isbn = this.isbn;
            book.title = this.title;
            book.author = this.author;
            book.edition = this.edition;
            book.price = this.price;
            book.status = this.status;
            return book;
        }
    }
}
