package nemi.in;

import java.util.ArrayList;

/**
 * Created by Aman on 5/27/2016.
 */
class BillItems {
    String id;
    String item;
    int qty = 0;
    int price;
    String imagepath;

    public BillItems(String id, String item, int qty, int price, String imagepath) {
        this.id = id;
        this.item = item;
        this.qty = qty;
        this.price = price;
        this.imagepath = imagepath;
    }

    public String getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public int getQty() {
        return qty;
    }

    public int getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    @Override
    public String toString() {
        return "BillItems{" +
                "id='" + id + '\'' +
                ", item='" + item + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", imagepath='" + imagepath + '\'' +
                '}';
    }
}