package Command_Processor;

import Entity.Book;
import Entity.TextBook;
import repository.BookRepository;

public class AddBookCommand extends Command {

    private BookRepository bookRepository;
    private Book book;

    public AddBookCommand(Book book, BookRepository bookRepository) {

        this.bookRepository = bookRepository;

        //
        if (book instanceof TextBook) {
            TextBook textBook = (TextBook) book;
            this.book = textBook;
        } else {
            this.book = book;
        }
    }

    @Override
    public void execute() {
        bookRepository.addBook(book);
    }
}
