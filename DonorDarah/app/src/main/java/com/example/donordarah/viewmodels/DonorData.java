package com.example.donordarah.viewmodels;

public class DonorData {

    private int totalDonate;
    private String lastDonate, name, contact, uid, address;

    public DonorData() {}

    public DonorData(int totalDonate, String lastDonate, String name, String contact, String address, String uid) {
        this.totalDonate = totalDonate;
        this.lastDonate = lastDonate;
        this.name = name;
        this.contact = contact;
        this.uid = uid;
        this.address = address;
    }

    public int getTotalDonate() {
        return totalDonate;
    }

    public void setTotalDonate(int totalDonate) {
        this.totalDonate = totalDonate;
    }

    public String getLastDonate() {
        return lastDonate;
    }

    public void setLastDonate(String lastDonate) {
        this.lastDonate = lastDonate;
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
