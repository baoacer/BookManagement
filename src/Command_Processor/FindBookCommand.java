package Command_Processor;

import Entity.Book;
import repository.BookRepository;
import repository.BookRepositoryImpl;

public class FindBookCommand extends Command {

    private BookRepository bookRepository;
    private int bookId;
    private Book result;

    public FindBookCommand(int bookId) {
        this.bookRepository = new BookRepositoryImpl();
        this.bookId = bookId;
    }

    @Override
    public void execute() {
        result = bookRepository.getBookById(bookId);
    }

    public Book getResult() {
        return result;
    }

}
