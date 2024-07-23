package Command_Processor;

import repository.BookRepository;

public class CommandProcessor {

    private static CommandProcessor commandProcessor = null;
    private BookRepository bookRepository;

    private CommandProcessor() {
    }

    public static CommandProcessor makeCommandProcessor() {
        if (commandProcessor == null) {
            commandProcessor = new CommandProcessor();
        }
        return commandProcessor;
    }

    // method
    public void execute(Command command) {
        command.execute();
    }

}
