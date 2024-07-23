package repository;

import java.util.List;

import Entity.Book;

public interface BookRepository {
    void addBook(Book book);

    Book getBookById(int id);

    List<Book> getAllBooks();

    void updateBook(Book book);

    void removeBook(int id);

    List<Book> findBooksByName(String name);
}
