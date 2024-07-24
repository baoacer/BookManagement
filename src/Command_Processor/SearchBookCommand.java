package Command_Processor;

import java.util.List;

import Entity.Book;
import repository.BookRepository;

public class SearchBookCommand extends Command {

    private BookRepository bookRepository;
    private String search;
    private List<Book> results;

    public SearchBookCommand(String search, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.search = search;
    }

    @Override
    public void execute() {
        results = bookRepository.findBooksByName(search);
    }

    public List<Book> getResults() {
        return results;
    }

}
