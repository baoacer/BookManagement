package Entity;

import java.sql.Date;

public class ReferenceBook extends Book {
    private double tax;

    public ReferenceBook(int id, String name, Date entryDate, double unitPrice, int quantity, String publisher,
            double tax) {
        super(id, name, entryDate, publisher, quantity, unitPrice);
        this.tax = tax;
        calculateTotalPrice();
    }

    public ReferenceBook(int id, String name, Date entryDate, double unitPrice, int quantity, String publisher,
            double tax, double totalPrice) {
        super(id, name, entryDate, publisher, quantity, unitPrice, totalPrice);
        this.tax = tax;
        calculateTotalPrice();
    }

    // Getter và Setter
    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    @Override
    public double averageUnitPrice() {
        throw new UnsupportedOperationException("Unimplemented method 'averageUnitPrice'");
    }

    @Override
    public void calculateTotalPrice() {
        setTotalPrice(getQuantity() * getUnitPrice() + tax);
    }

    @Override
    public String getBookType() {
        return "ReferenceBook";
    }

}
