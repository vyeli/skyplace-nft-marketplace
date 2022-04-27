package ar.edu.itba.paw.model;

public class Image {
    private byte[] image;

    public Image(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
