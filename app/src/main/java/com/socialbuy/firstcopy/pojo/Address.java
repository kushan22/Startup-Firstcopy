package com.socialbuy.firstcopy.pojo;

/**
 * Created by kushansingh on 05/11/17.
 */

public class Address {

    String fullName, addressLine,city,state,pincode,phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getCity() {
        return city;
    }

    public String getPincode() {
        return pincode;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

