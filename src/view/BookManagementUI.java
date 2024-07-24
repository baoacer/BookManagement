package view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Command_Processor.AddBookCommand;
import Command_Processor.Command;
import Command_Processor.CommandProcessor;
import Command_Processor.GetAllBookCommand;
import Command_Processor.RemoveBookCommand;
import Command_Processor.SearchBookCommand;
import Command_Processor.UpdateBookCommand;
import Entity.Book;
import Entity.ReferenceBook;
import Entity.TextBook;
import observer.Subscriber;
import repository.BookRepository;

import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class BookManagementUI extends JFrame implements Subscriber {

    private CommandProcessor commandProcessor;
    private BookRepository bookRepository;

    // Field
    // Table
    private DefaultTableModel tableModel;
    private JTable bookTable;

    // JLabel
    private JLabel idLabel, nameLabel, entryDateLabel, unitPriceLabel, quantityLabel, publisherLabel, bookTypeLabel,
            conditionLabel, taxLabel;

    // JTextField
    private JTextField idField, nameField, entryDateField, unitPriceField, quantityField, publisherField, taxField;

    // JComboBox
    private JComboBox<String> bookTypeComboBox, conditionComboBox;

    // JButton
    private JButton addButton, removeButton, editButton, findButton;

    // Search
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;

    public BookManagementUI(CommandProcessor commandProcessor, BookRepository repository) {

        this.commandProcessor = commandProcessor;
        this.bookRepository = repository;

        // Initialize components
        initializeComponents();

        // Setup Layout
        setupLayout();

        // Set JFrame properties
        this.setTitle("Book Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setVisible(true);

        loadBooksFromDatabase();
    }

    private void initializeComponents() {
        // Initialize labels
        idLabel = new JLabel("ID:");
        nameLabel = new JLabel("Name:");
        entryDateLabel = new JLabel("Entry Date:");
        unitPriceLabel = new JLabel("Unit Price:");
        quantityLabel = new JLabel("Quantity:");
        publisherLabel = new JLabel("Publisher:");
        bookTypeLabel = new JLabel("Book Style:");
        conditionLabel = new JLabel("Condition:");
        taxLabel = new JLabel("Tax:");

        // Initialize text fields
        idField = new JTextField(20);
        nameField = new JTextField(20);
        entryDateField = new JTextField(20);
        unitPriceField = new JTextField(20);
        quantityField = new JTextField(20);
        publisherField = new JTextField(20);
        taxField = new JTextField(20);

        // Initialize combo box
        String[] bookTypes = { "TextBook", "ReferenceBook" };
        bookTypeComboBox = new JComboBox<>(bookTypes);
        bookTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleTaxAndConditionFields();
            }
        });

        // Initialize condition combo box
        String[] conditions = { "New", "Old" };
        conditionComboBox = new JComboBox<>(conditions);

        // Initialize buttons
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        editButton = new JButton("Edit");
        findButton = new JButton("Find");

        // Initialize search components
        searchLabel = new JLabel("Search by Name:");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        // Initialize table
        String[] columnNames = { "ID", "Name", "Entry Date", "Unit Price", "Quantity", "Publisher", "Book Style",
                "Condition",
                "Tax", "Total Price" };
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);

        // Add button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });

        // Add selection listener to the table
        bookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                    int selectedRow = bookTable.getSelectedRow();
                    idField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
                    nameField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
                    entryDateField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
                    unitPriceField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
                    quantityField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
                    publisherField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 5)));
                    bookTypeComboBox.setSelectedItem(String.valueOf(tableModel.getValueAt(selectedRow, 6)));
                    conditionComboBox.setSelectedItem(String.valueOf(tableModel.getValueAt(selectedRow, 7)));
                    taxField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 8)));
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBook();
            }
        });

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findBook();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBook();
            }
        });

    }

    public void addBook() {
        try {

            // Get value from Text Field
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = dateFormat.parse(entryDateField.getText());
            java.sql.Date entryDate = new java.sql.Date(utilDate.getTime());
            String publisher = publisherField.getText();
            String bookType = (String) bookTypeComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            double unitPrice = Double.parseDouble(unitPriceField.getText());
            double tax = taxField.isVisible() ? Double.parseDouble(taxField.getText()) : 0;

            Book book = null;

            if ("TextBook".equals(bookType)) {
                String condition = (String) conditionComboBox.getSelectedItem();
                book = new TextBook(id, name, entryDate, unitPrice, quantity, publisher, condition);
            } else {
                book = new ReferenceBook(id, name, entryDate, unitPrice, quantity, publisher, tax);
            }

            Command command = new AddBookCommand(book, bookRepository);
            commandProcessor.execute(command);

            // Update table alter add Book
            int index = findRowById(id);
            if (index == -1) {
                tableModel.addRow(new Object[] {
                        book.getId(),
                        book.getName(),
                        book.getEntryDate(),
                        book.getUnitPrice(),
                        book.getQuantity(),
                        book.getPublisher(),
                        bookType,
                        book instanceof TextBook ? ((TextBook) book).getCondition() : "",
                        book instanceof ReferenceBook ? ((ReferenceBook) book).getTax() : "",
                        book.getTotalPrice()
                });
            } else {
                JOptionPane.showMessageDialog(null, "Duplicate book ID!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Check Id exists?
    private int findRowById(int id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int bookId = (int) tableModel.getValueAt(i, 0);
            if (bookId == id) {
                return i;
            }
        }
        return -1;
    }

    public void removeBook() {
        try {

            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to remove.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Command command = new RemoveBookCommand(id, bookRepository);
            commandProcessor.execute(command);

            tableModel.removeRow(selectedRow);

            JOptionPane.showMessageDialog(this, "Book removed successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error removing book: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateBook() {
        try {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a book to update.");
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = dateFormat.parse(entryDateField.getText());
            java.sql.Date entryDate = new java.sql.Date(utilDate.getTime());
            String publisher = publisherField.getText();
            String bookType = (String) bookTypeComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());
            double unitPrice = Double.parseDouble(unitPriceField.getText());
            double tax = taxField.isVisible() ? Double.parseDouble(taxField.getText()) : 0;

            Book book = null;
            if ("TextBook".equals(bookType)) {
                String condition = (String) conditionComboBox.getSelectedItem();
                book = new TextBook(id, name, entryDate, unitPrice, quantity, publisher, condition);
            } else {
                book = new ReferenceBook(id, name, entryDate, unitPrice, quantity, publisher, tax);
            }

            Command command = new UpdateBookCommand(book, bookRepository);
            commandProcessor.execute(command);

            tableModel.setValueAt(book.getId(), selectedRow, 0);
            tableModel.setValueAt(book.getName(), selectedRow, 1);
            tableModel.setValueAt(book.getEntryDate(), selectedRow, 2);
            tableModel.setValueAt(book.getUnitPrice(), selectedRow, 3);
            tableModel.setValueAt(book.getQuantity(), selectedRow, 4);

            tableModel.setValueAt(book.getPublisher(), selectedRow, 5);
            tableModel.setValueAt(bookType, selectedRow, 6);
            tableModel.setValueAt(book instanceof TextBook ? ((TextBook) book).getCondition() : "", selectedRow, 7);
            tableModel.setValueAt(book instanceof ReferenceBook ? ((ReferenceBook) book).getTax() : "", selectedRow, 8);
            tableModel.setValueAt(book.getTotalPrice(), selectedRow, 9);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void findBook() {
        try {
            int id = Integer.parseInt(idField.getText());
            Book book = bookRepository.getBookById(id);

            if (book != null) {
                idField.setText(String.valueOf(book.getId()));
                nameField.setText(book.getName());
                entryDateField.setText(book.getEntryDate().toString());
                publisherField.setText(book.getPublisher());
                bookTypeComboBox.setSelectedItem(book instanceof TextBook ? "TextBook" : "ReferenceBook");

                if (book instanceof TextBook) {
                    TextBook textBook = (TextBook) book;
                    conditionComboBox.setSelectedItem(textBook.getCondition());
                    quantityField.setText(String.valueOf(textBook.getQuantity()));
                    unitPriceField.setText(String.valueOf(textBook.getUnitPrice()));
                } else if (book instanceof ReferenceBook) {
                    ReferenceBook referenceBook = (ReferenceBook) book;
                    taxField.setText(String.valueOf(referenceBook.getTax()));
                    quantityField.setText(String.valueOf(referenceBook.getQuantity()));
                    unitPriceField.setText(String.valueOf(referenceBook.getUnitPrice()));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Book not found.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchBook() {
        try {
            String name = searchField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }

            Command command = new SearchBookCommand(name, bookRepository);
            commandProcessor.execute(command);
            List<Book> books = ((SearchBookCommand) command).getResults();

            // Clear existing data in table
            tableModel.setRowCount(0);

            // Add found books to the table
            for (Book book : books) {
                Object[] rowData = new Object[10];
                rowData[0] = book.getId();
                rowData[1] = book.getName();
                rowData[2] = book.getEntryDate();
                rowData[3] = book.getUnitPrice();
                rowData[4] = book.getQuantity();
                rowData[5] = book.getPublisher();
                rowData[6] = book instanceof TextBook ? "TextBook" : "ReferenceBook";
                rowData[7] = book instanceof TextBook ? ((TextBook) book).getCondition() : "";
                rowData[8] = book instanceof ReferenceBook ? ((ReferenceBook) book).getTax() : "";
                rowData[9] = book.getTotalPrice();
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleTaxAndConditionFields() {
        String selectedType = (String) bookTypeComboBox.getSelectedItem();
        boolean isTextBook = "TextBook".equals(selectedType);
        taxLabel.setVisible(!isTextBook);
        taxField.setVisible(!isTextBook);
        conditionLabel.setVisible(isTextBook);
        conditionComboBox.setVisible(isTextBook);
    }

    private void setupLayout() {
        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addComponentsToPanel(inputPanel, gbc);

        return inputPanel;
    }

    private void addComponentsToPanel(JPanel panel, GridBagConstraints gbc) {
        panel.add(idLabel, gbc);
        gbc.gridx++;
        panel.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(nameLabel, gbc);
        gbc.gridx++;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(entryDateLabel, gbc);
        gbc.gridx++;
        panel.add(entryDateField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(unitPriceLabel, gbc);
        gbc.gridx++;
        panel.add(unitPriceField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(quantityLabel, gbc);
        gbc.gridx++;
        panel.add(quantityField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(publisherLabel, gbc);
        gbc.gridx++;
        panel.add(publisherField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(bookTypeLabel, gbc);
        gbc.gridx++;
        panel.add(bookTypeComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(conditionLabel, gbc);
        gbc.gridx++;
        panel.add(conditionComboBox, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(taxLabel, gbc);
        gbc.gridx++;
        panel.add(taxField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(searchLabel, gbc);
        gbc.gridx++;
        panel.add(searchField, gbc);
        gbc.gridx++;
        panel.add(searchButton, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(findButton);
        return buttonPanel;
    }

    private void loadBooksFromDatabase() {

        Command command = new GetAllBookCommand(bookRepository);
        commandProcessor.execute(command);

        List<Book> books = ((GetAllBookCommand) command).getResult();

        for (Book book : books) {
            if (book instanceof TextBook) {
                TextBook textBook = (TextBook) book;
                tableModel.addRow(new Object[] {
                        textBook.getId(),
                        textBook.getName(),
                        textBook.getEntryDate(),
                        textBook.getUnitPrice(),
                        textBook.getQuantity(),
                        textBook.getPublisher(),
                        textBook.getBookType(),
                        textBook.getCondition(),
                        "",
                        textBook.getTotalPrice()
                });
            } else if (book instanceof ReferenceBook) {
                ReferenceBook referenceBook = (ReferenceBook) book;
                tableModel.addRow(new Object[] {
                        referenceBook.getId(),
                        referenceBook.getName(),
                        referenceBook.getEntryDate(),
                        referenceBook.getUnitPrice(),
                        referenceBook.getQuantity(),
                        referenceBook.getPublisher(),
                        referenceBook.getBookType(),
                        "",
                        referenceBook.getTax(),
                        referenceBook.getTotalPrice()
                });
            }
        }
    }

    @Override
    public void update() {
        tableModel.setRowCount(0); // Clear existing data
        loadBooksFromDatabase(); // Reload data from database
    }
}
