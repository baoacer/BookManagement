package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Command_Processor.AddBookCommand;
import Command_Processor.Command;
import Command_Processor.CommandProcessor;
import Command_Processor.GetAllBookCommand;
import Entity.Book;
import observer.Subscriber;

import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class BookManagementUI extends JFrame implements Subscriber {

    private CommandProcessor commandProcessor;

    // Field
    // Table
    private DefaultTableModel tableModel;
    private JTable bookTable;

    // JLabel
    private JLabel idLabel, entryDateLabel, unitPriceLabel, quantityLabel, publisherLabel, bookTypeLabel,
            conditionLabel, taxLabel, totalPriceLabel;

    // JTextField
    private JTextField idField, entryDateField, unitPriceField, quantityField, publisherField, taxField,
            totalPriceField;

    // JComboBox
    private JComboBox<String> bookTypeComboBox, conditionComboBox;

    // JButton
    private JButton addButton, removeButton, editButton, findButton, calculateButton;

    // Search
    private JLabel searchLabel;
    private JTextField searchField;
    private JButton searchButton;

    public BookManagementUI(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;

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
        entryDateLabel = new JLabel("Entry Date:");
        unitPriceLabel = new JLabel("Unit Price:");
        quantityLabel = new JLabel("Quantity:");
        publisherLabel = new JLabel("Publisher:");
        bookTypeLabel = new JLabel("Book Style:");
        conditionLabel = new JLabel("Condition:");
        taxLabel = new JLabel("Tax:");
        totalPriceLabel = new JLabel("Total Price:");

        // Initialize text fields
        idField = new JTextField(20);
        entryDateField = new JTextField(20);
        unitPriceField = new JTextField(20);
        quantityField = new JTextField(20);
        publisherField = new JTextField(20);
        taxField = new JTextField(20);
        totalPriceField = new JTextField(20);

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
        calculateButton = new JButton("Calculate Total Price");

        // Initialize search components
        searchLabel = new JLabel("Search by ID:");
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        // Initialize table
        String[] columnNames = { "ID", "Entry Date", "Unit Price", "Quantity", "Publisher", "Book Style", "Condition",
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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBook();
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

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTotalPrice();
            }
        });
    }

    public void addBook() {
        try {
            int id = Integer.parseInt(idField.getText());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = dateFormat.parse(entryDateField.getText());
            java.sql.Date entryDate = new java.sql.Date(utilDate.getTime());
            double unitPrice = Double.parseDouble(unitPriceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            String publisher = publisherField.getText();
            String bookType = (String) bookTypeComboBox.getSelectedItem();
            String condition = (String) conditionComboBox.getSelectedItem();
            double tax = taxField.isVisible() ? Double.parseDouble(taxField.getText()) : 0;

            Book book = new Book(id, entryDate, unitPrice, quantity, publisher, bookType, condition, tax);

            Command command = new AddBookCommand(book);
            commandProcessor.execute(command);

            // Update table alter add book
            int rowIndex = findRowById(id);
            if (rowIndex != -1) {
                tableModel.setValueAt(entryDate, rowIndex, 1);
                tableModel.setValueAt(unitPrice, rowIndex, 2);
                tableModel.setValueAt(quantity, rowIndex, 3);
                tableModel.setValueAt(publisher, rowIndex, 4);
                tableModel.setValueAt(bookType, rowIndex, 5);
                tableModel.setValueAt(condition, rowIndex, 6);
                tableModel.setValueAt(tax, rowIndex, 7);
                tableModel.setValueAt(book.getTotalPrice(), rowIndex, 8);
            } else {
                tableModel.addRow(new Object[] { id, entryDate, unitPrice, quantity, publisher, bookType, condition,
                        tax, book.getTotalPrice() });
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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
        // Code for removing a book
    }

    public void editBook() {
        // Code for editing a book
    }

    public void findBook() {
        // Code for finding a book
    }

    public void searchBook() {
        // Code for searching a book
    }

    private void toggleTaxAndConditionFields() {
        String selectedType = (String) bookTypeComboBox.getSelectedItem();
        boolean isTextBook = "TextBook".equals(selectedType);
        taxLabel.setVisible(!isTextBook);
        taxField.setVisible(!isTextBook);
        conditionLabel.setVisible(isTextBook);
        conditionComboBox.setVisible(isTextBook);
    }

    private void calculateTotalPrice() {
        try {
            double unitPrice = Double.parseDouble(unitPriceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            double tax = taxField.isVisible() ? Double.parseDouble(taxField.getText()) : 0;

            double totalPrice = unitPrice * quantity + tax;
            totalPriceField.setText(String.valueOf(totalPrice));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Unit Price, Quantity, and Tax.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
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
        panel.add(totalPriceLabel, gbc);
        gbc.gridx++;
        panel.add(totalPriceField, gbc);
        gbc.gridx++;
        panel.add(calculateButton, gbc); // Add calculate button next to total price field

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
        Command command = new GetAllBookCommand();
        commandProcessor.execute(command);
        List<Book> books = ((GetAllBookCommand) command).getResult();
        for (Book book : books) {
            tableModel.addRow(new Object[] {
                    book.getId(),
                    book.getEntryDate(),
                    book.getUnitPrice(),
                    book.getQuantity(),
                    book.getPublisher(),
                    book.getBookType(),
                    book.getCondition(),
                    book.getTax(),
                    book.getTotalPrice()
            });
        }
    }

    @Override
    public void update() {
        tableModel.setRowCount(0); // Clear existing data
        loadBooksFromDatabase(); // Reload data from database
    }
}
