package Entity;

import java.sql.Date;

public class TextBook extends Book {

    // Field
    private String condition;

    // Constructors
    public TextBook(int id, String name, Date entryDate, double unitPrice, int quantity, String publisher,
            String condition) {
        super(id, name, entryDate, publisher, quantity, unitPrice);
        this.condition = condition;
        calculateTotalPrice();
    }

    public TextBook(int id, String name, Date entryDate, double unitPrice, int quantity, String publisher,
            String condition, double totalPrice) {
        super(id, name, entryDate, publisher, quantity, unitPrice, totalPrice);
        this.condition = condition;
        calculateTotalPrice();
    }

    // Getter và Setter
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Method Abstract
    @Override
    public void calculateTotalPrice() {
        if ("New".equalsIgnoreCase(condition)) {
            setTotalPrice(getQuantity() * getUnitPrice());
        } else {
            setTotalPrice(getQuantity() * getUnitPrice() * 0.5);
        }
    }

    @Override
    public String getBookType() {
        return "TextBook";
    }

    @Override
    public double averageUnitPrice() {
        throw new UnsupportedOperationException("Unimplemented method 'averageUnitPrice'");
    }
}
