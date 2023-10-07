package com.example.chapter04;

import java.util.HashMap;
import java.util.Map;

public class Productinfo {
    protected String rfid;
    protected String name;
    protected String price;


    public Productinfo(String rfid, String name, String price) {
        this.rfid = rfid;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Productinfo{" +
                "rfid='" + rfid + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
