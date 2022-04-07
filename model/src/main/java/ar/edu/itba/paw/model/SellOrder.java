package ar.edu.itba.paw.model;

public class SellOrder {

    private long id;
    private String name;
    private double price;
    private String description;
    private byte[] image;
    private String email;

    public SellOrder(String name, double price, String description, byte[] image, String email) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }
}
