package com.socialbuy.firstcopy.pojo;

/**
 * Created by kushansingh on 09/11/17.
 */

public class OrderDetails {

    String productName,productImage,status,date,orderid,price,size;

    public String getDate() {
        return date;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getPrice() {
        return price;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getStatus() {
        return status;
    }

    public String getSize() {
        return size;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
