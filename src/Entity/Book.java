package Entity;

import java.util.Date;

public abstract class Book {
    private int id;
    private String name;
    private Date entryDate;
    private String publisher;
    private double totalPrice;
    private int quantity;
    private double unitPrice;

    public Book(int id, String name, Date entryDate, String publisher, int quantity, double unitPrice) {
        this.id = id;
        this.name = name;
        this.entryDate = entryDate;
        this.publisher = publisher;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Book(int id, String name, Date entryDate, String publisher, int quantity, double unitPrice,
            double totalPrice) {
        this.id = id;
        this.name = name;
        this.entryDate = entryDate;
        this.publisher = publisher;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getName() {
        return name;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    protected void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Abstract methods
    public abstract void calculateTotalPrice();

    public abstract String getBookType();

    public abstract double averageUnitPrice();

}
