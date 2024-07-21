package Entity;

import java.sql.Date;

import observer.Publisher;

public class Book extends Publisher {
    private int id;
    private Date entryDate;
    private double unitPrice;
    private int quantity;
    private String publisher;
    private String bookType;
    private String condition;
    private double tax;
    private double totalPrice;

    // constructors
    public Book() {
    };

    public Book(int id, Date entryDate, double unitPrice, int quantity, String publisher, String bookType,
            String condition, double tax, double totalPrice) {
        this.id = id;
        this.entryDate = entryDate;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.publisher = publisher;
        this.bookType = bookType;
        this.condition = condition;
        this.tax = tax;
        this.totalPrice = totalPrice;
    }

    public Book(int id, Date entryDate, double unitPrice, int quantity, String publisher, String bookType,
            String condition, double tax) {
        this.id = id;
        this.entryDate = entryDate;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.publisher = publisher;
        this.bookType = bookType;
        this.condition = condition;
        this.tax = tax;
        this.totalPrice = this.quantity * this.unitPrice + this.tax;
    }

    // getter
    public int getId() {
        return id;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getBookType() {
        return bookType;
    }

    public String getCondition() {
        return condition;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // functions, methods
    public void textBookNew() {
        this.totalPrice = this.quantity * this.unitPrice;
        changeStatus();
    }

    public void textBookOld() {
        this.totalPrice = this.quantity * this.unitPrice * 0.5;
        changeStatus();
    }

    public void referenceBook() {
        this.totalPrice = this.quantity * this.unitPrice + this.tax;
        changeStatus();
    }

    public void changeStatus() {
        notifySubscribers();
    }

    @Override
    public void notifySubscribers() {
        super.notifySubscribers();
    }

}
