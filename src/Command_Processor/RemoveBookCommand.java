package Command_Processor;

import repository.BookRepository;

public class RemoveBookCommand extends Command {

    private BookRepository bookRepository;
    private int id;

    public RemoveBookCommand(int id, BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.id = id;
    }

    @Override
    public void execute() {
        bookRepository.removeBook(id);
    }

}
