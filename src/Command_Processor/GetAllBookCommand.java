package Command_Processor;

import java.util.List;

import Entity.Book;
import repository.BookRepository;

public class GetAllBookCommand extends Command {

    private BookRepository bookRepository;
    private List<Book> result;

    public GetAllBookCommand(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void execute() {
        result = bookRepository.getAllBooks();
    }

    public List<Book> getResult() {
        return result;
    }

}
