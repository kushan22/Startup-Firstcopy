package com.socialbuy.firstcopy;

/**
 * Created by kushansingh on 03/08/17.
 */

public class Products {

    String userName,imageUrl,profileLink,productDesc,dateofcreation,numberofdays,sellerBio,sellerPhoneNumber;

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getProfileLink() {
        return profileLink;
    }
    public String getNumberofdays(){
        return numberofdays;
    }

    public String getDateofcreation(){
        return dateofcreation;
    }

    public String getSellerBio(){
        return sellerBio;
    }

    public String getSellerPhoneNumber(){
        return sellerPhoneNumber;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public void setDateofcreation(String dateofcreation){
        this.dateofcreation = dateofcreation;
    }

    public void setNumberofdays(String numberofdays){
        this.numberofdays = numberofdays;
    }

    public void setSellerBio(String sellerBio){
        this.sellerBio = sellerBio;
    }

    public void setSellerPhoneNumber(String sellerPhoneNumber){
        this.sellerPhoneNumber  = sellerPhoneNumber;
    }
}
