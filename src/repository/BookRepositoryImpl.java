package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Entity.Book;
import Entity.ReferenceBook;
import Entity.TextBook;

public class BookRepositoryImpl implements BookRepository {

    // Connect database
    private final String url = "jdbc:mysql://localhost:3306/book_management";
    private final String user = "root";
    private final String password = "root";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // Method Add a new Book
    @Override
    public void addBook(Book book) {
        String sql = "INSERT INTO books (id, name, entryDate, unitPrice, quantity, publisher, bookType, conditions, tax, totalPrice) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setInt(1, book.getId());
            pstmt.setString(2, book.getName());
            pstmt.setDate(3, new java.sql.Date(book.getEntryDate().getTime()));
            if (book instanceof TextBook) {
                TextBook textBook = (TextBook) book;
                pstmt.setDouble(4, textBook.getUnitPrice());
                pstmt.setInt(5, textBook.getQuantity());
                pstmt.setString(7, textBook.getBookType());
                pstmt.setString(8, textBook.getCondition());
                pstmt.setDouble(9, 0);
            } else if (book instanceof ReferenceBook) {
                ReferenceBook referenceBook = (ReferenceBook) book;
                pstmt.setDouble(4, referenceBook.getUnitPrice());
                pstmt.setInt(5, referenceBook.getQuantity());
                pstmt.setString(7, referenceBook.getBookType());
                pstmt.setString(8, "");
                pstmt.setDouble(9, referenceBook.getTax());
            }
            pstmt.setString(6, book.getPublisher());
            pstmt.setDouble(10, book.getTotalPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method Get Book By Id
    @Override
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        Book book = null;

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                String bookType = result.getString("bookType");
                if ("TextBook".equalsIgnoreCase(bookType)) {
                    book = new TextBook(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getDate("entryDate"),
                            result.getDouble("unitPrice"),
                            result.getInt("quantity"),
                            result.getString("publisher"),
                            result.getString("conditions"),
                            result.getDouble("totalPrice"));
                } else if ("ReferenceBook".equalsIgnoreCase(bookType)) {
                    book = new ReferenceBook(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getDate("entryDate"),
                            result.getDouble("unitPrice"),
                            result.getInt("quantity"),
                            result.getString("publisher"),
                            result.getDouble("tax"),
                            result.getDouble("totalPrice"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return book;
    }

    // Method Get All Book
    @Override
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection connect = connect();
                Statement stmt = connect.createStatement();
                ResultSet result = stmt.executeQuery(sql)) {

            while (result.next()) {
                String bookType = result.getString("bookType");
                Book book = null;
                if ("TextBook".equalsIgnoreCase(bookType)) {
                    book = new TextBook(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getDate("entryDate"),
                            result.getDouble("unitPrice"),
                            result.getInt("quantity"),
                            result.getString("publisher"),
                            result.getString("conditions"),
                            result.getDouble("totalPrice"));
                } else if ("ReferenceBook".equalsIgnoreCase(bookType)) {
                    book = new ReferenceBook(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getDate("entryDate"),
                            result.getDouble("unitPrice"),
                            result.getInt("quantity"),
                            result.getString("publisher"),
                            result.getDouble("tax"),
                            result.getDouble("totalPrice"));
                }
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return books;
    }

    // Method Update Book
    @Override
    public void updateBook(Book book) {
        String sql = "UPDATE books SET name = ?, entryDate = ?, unitPrice = ?, quantity = ?, publisher = ?, bookType = ?, conditions = ?, tax = ?, totalPrice = ? WHERE id = ?";

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {

            pstmt.setString(1, book.getName());
            pstmt.setDate(2, new java.sql.Date(book.getEntryDate().getTime()));

            if (book instanceof TextBook) {
                TextBook textBook = (TextBook) book;
                pstmt.setDouble(3, textBook.getUnitPrice());
                pstmt.setInt(4, textBook.getQuantity());
                pstmt.setString(7, textBook.getCondition());
                pstmt.setDouble(8, 0);
            } else if (book instanceof ReferenceBook) {
                ReferenceBook referenceBook = (ReferenceBook) book;
                pstmt.setDouble(3, referenceBook.getUnitPrice());
                pstmt.setInt(4, referenceBook.getQuantity());
                pstmt.setDouble(8, referenceBook.getTax());
                pstmt.setString(7, "");
            }
            pstmt.setString(5, book.getPublisher());
            pstmt.setString(6, book.getBookType());
            pstmt.setDouble(9, book.getTotalPrice());
            pstmt.setInt(10, book.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method Remove Book
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

    // Method Find Books By Name
    @Override
    public List<Book> findBooksByName(String name) {
        String sql = "SELECT * FROM books WHERE name LIKE ?";
        List<Book> books = new ArrayList<>();

        try (Connection connect = connect();
                PreparedStatement pstmt = connect.prepareStatement(sql)) {

            pstmt.setString(1, "%" + name + "%");
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {

                String bookType = result.getString("bookType");
                Book book = null;

                if ("TextBook".equalsIgnoreCase(bookType)) {
                    book = new TextBook(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getDate("entryDate"),
                            result.getDouble("unitPrice"),
                            result.getInt("quantity"),
                            result.getString("publisher"),
                            result.getString("conditions"),
                            result.getDouble("totalPrice"));
                } else if ("ReferenceBook".equalsIgnoreCase(bookType)) {
                    book = new ReferenceBook(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getDate("entryDate"),
                            result.getDouble("unitPrice"),
                            result.getInt("quantity"),
                            result.getString("publisher"),
                            result.getDouble("tax"),
                            result.getDouble("totalPrice"));
                }

                books.add(book);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return books;
    }
}
