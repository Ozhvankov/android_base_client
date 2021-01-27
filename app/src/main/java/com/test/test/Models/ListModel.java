package com.test.test.Models;

public class ListModel {
    public int id;
    public String inboundShipmentNumber, items, articles, status;

    public ListModel(int id, String inboundShipmentNumber, String items, String articles, String status) {
        this.id = id;
        this.inboundShipmentNumber = inboundShipmentNumber;
        this.items = items;
        this.articles = articles;
        this.status = status;
    }
}
