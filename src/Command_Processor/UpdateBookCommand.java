package Command_Processor;

import Entity.Book;
import repository.BookRepository;
import repository.BookRepositoryImpl;

public class UpdateBookCommand extends Command {

    private BookRepository bookRepository;
    private Book book;

    public UpdateBookCommand(Book book) {
        this.bookRepository = new BookRepositoryImpl();
        this.book = book;
    }

    @Override
    public void execute() {
        bookRepository.updateBook(book);
    }

}
