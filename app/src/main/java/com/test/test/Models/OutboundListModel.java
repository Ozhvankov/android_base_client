package com.test.test.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class OutboundListModel implements Serializable {
    public String id;
    private String mOutboundShipmentNumber;
    public String items;

    public OutboundListModel(String id, String OutboundShipmentNumber, String items) {
        this.id = id;
        this.mOutboundShipmentNumber = OutboundShipmentNumber;
        this.items = items;
    }

    @NonNull
    @Override
    public String toString() {
        return mOutboundShipmentNumber + " (" + items + ")";
    }
}
