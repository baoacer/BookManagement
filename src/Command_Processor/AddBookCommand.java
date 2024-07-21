package Command_Processor;

import Entity.Book;
import repository.BookRepository;
import repository.BookRepositoryImpl;

public class AddBookCommand extends Command {

    private BookRepository bookRepository;
    private Book book;

    public AddBookCommand(Book book) {
        this.bookRepository = new BookRepositoryImpl();

        if (book.getCondition() == "New") {
            book.textBookNew();
        } else {
            book.textBookOld();
        }

        this.book = book;

    }

    @Override
    public void execute() {
        bookRepository.addBook(book);
    }

}
