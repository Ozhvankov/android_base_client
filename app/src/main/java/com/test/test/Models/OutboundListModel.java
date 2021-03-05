package com.test.test.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class OutboundListModel implements Serializable {
    public int id, status_id;
    private String mOutboundShipmentNumber, Supplier, Client;
    public String items;

    public OutboundListModel(int id, String OutboundShipmentNumber, String items, String Supplier, String Client, int status_id) {
        this.id = id;
        this.mOutboundShipmentNumber = OutboundShipmentNumber;
        this.items = items;
        this.status_id = status_id;
        this.Supplier = Supplier;
        this.Client = Client;
    }

    @NonNull
    @Override
    public String toString() {
        return mOutboundShipmentNumber + " (" + items + ")";
    }
}
