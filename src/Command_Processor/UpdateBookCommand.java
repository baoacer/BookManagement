package Command_Processor;

import Entity.Book;
import repository.BookRepository;

public class UpdateBookCommand extends Command {

    private BookRepository bookRepository;
    private Book book;

    public UpdateBookCommand(Book book, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.book = book;
    }

    @Override
    public void execute() {
        bookRepository.updateBook(book);
    }

}
