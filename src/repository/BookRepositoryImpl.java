package repository;

import Entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepositoryImpl implements BookRepository {
    private final String url = "jdbc:mysql://localhost:3306/book_management";
    private final String user = "root";
    private final String password = "root";

    // connection to the database
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Method to add a book to the database
    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (id, entryDate, unitPrice, quantity, publisher, bookType, conditions, tax, totalPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setInt(1, book.getId());
            pstmt.setDate(2, book.getEntryDate());
            pstmt.setDouble(3, book.getUnitPrice());
            pstmt.setInt(4, book.getQuantity());
            pstmt.setString(5, book.getPublisher());
            pstmt.setString(6, book.getBookType());
            pstmt.setString(7, book.getCondition());
            pstmt.setDouble(8, book.getTax());
            pstmt.setDouble(9, book.getTotalPrice());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to get a book by ID
    @Override
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        Book book = null;

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                book = new Book(
                        result.getInt("id"),
                        result.getDate("entryDate"),
                        result.getDouble("unitPrice"),
                        result.getInt("quantity"),
                        result.getString("publisher"),
                        result.getString("bookType"),
                        result.getString("conditions"),
                        result.getDouble("tax"),
                        result.getDouble("totalPrice"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return book;
    }

    // Method to get all books
    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection connect = connect();
                Statement stmt = connect.createStatement();
                ResultSet result = stmt.executeQuery(sql)) {

            while (result.next()) {
                Book book = new Book(
                        result.getInt("id"),
                        result.getDate("entryDate"),
                        result.getDouble("unitPrice"),
                        result.getInt("quantity"),
                        result.getString("publisher"),
                        result.getString("bookType"),
                        result.getString("conditions"),
                        result.getDouble("tax"),
                        result.getDouble("totalPrice"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return books;
    }

    // Method to update a book
    @Override
    public void updateBook(Book book) {
        String sql = "UPDATE books SET entryDate = ?, unitPrice = ?, quantity = ?, publisher = ?, bookType = ?, conditions = ?, tax = ?, totalPrice = ? WHERE id = ?";

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setDate(1, book.getEntryDate());
            pstmt.setDouble(2, book.getUnitPrice());
            pstmt.setInt(3, book.getQuantity());
            pstmt.setString(4, book.getPublisher());
            pstmt.setString(5, book.getBookType());
            pstmt.setString(6, book.getCondition());
            pstmt.setDouble(7, book.getTax());
            pstmt.setDouble(8, book.getTotalPrice());
            pstmt.setInt(9, book.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to delete a book by ID
    @Override
    public void removeBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
