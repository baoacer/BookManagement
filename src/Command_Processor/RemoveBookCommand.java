package Command_Processor;

import repository.BookRepository;
import repository.BookRepositoryImpl;

public class RemoveBookCommand extends Command {

    private BookRepository bookRepository;
    private int id;

    public RemoveBookCommand(int id) {
        this.bookRepository = new BookRepositoryImpl();
        this.id = id;
    }

    @Override
    public void execute() {
        bookRepository.removeBook(id);
    }

}
