package Command_Processor;

import java.util.List;

import Entity.Book;
import repository.BookRepository;
import repository.BookRepositoryImpl;

public class GetAllBookCommand extends Command {

    private BookRepository bookRepository;
    private List<Book> result;

    public GetAllBookCommand() {
        this.bookRepository = new BookRepositoryImpl();
    }

    @Override
    public void execute() {
        result = bookRepository.getAllBooks();
    }

    public List<Book> getResult() {
        return result;
    }

}
