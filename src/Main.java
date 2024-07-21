import Command_Processor.CommandProcessor;
import repository.BookRepository;
import repository.BookRepositoryImpl;
import view.BookManagementUI;

public class Main {
    public static void main(String[] args) {
        BookRepository bookRepository = new BookRepositoryImpl();
        CommandProcessor commandProcessor = CommandProcessor.makeCommandProcessor();
        BookManagementUI bookManagementUI = new BookManagementUI(commandProcessor);
    }
}
