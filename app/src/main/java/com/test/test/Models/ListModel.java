package com.test.test.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ListModel implements Parcelable {
    public int id;
    public String Warehouse, Supplier, Inbound_shipment_number, created_at, Item_articles,updated_at;
    public int Items_amount, Gate, Client, status_id;

    public static final int STATUS_INPUT_FACT = 1;
    public static final int STATUS_PUT_AWAY = 2;
    public static final int STATUS_PUT_AWAY_ACCEPT = 3;
/*
"id": 36,
"Inbound_shipment_number": "test_0901",
"Items_amount": 0,
"Gate": 0,
"Item_articles": "",
"updated_at": "2021-01-09 12:37:27",
"Supplier": 1,
"Client": 1,
"Warehouse": 1,
"created_at": "2021-01-09 12:37:27",
"status_id": 1
 */
    public ListModel(int id,
                     String Inbound_shipment_number,
                     int Items_amount,
                     int Gate,
                     String Item_articles,
                     String updated_at,
                     String Supplier,
                     int Client,
                     String Warehouse,
                     String created_at,
                     int status_id) {
        this.id = id;
        this.Inbound_shipment_number = Inbound_shipment_number;
        this.created_at = created_at;
        this.Supplier = Supplier;
        this.Items_amount = Items_amount;
        this.Item_articles = Item_articles;
        this.updated_at = updated_at;
        this.Client = Client;
        this.Warehouse = Warehouse;
        this.Gate = Gate;
        this.status_id = status_id;
    }


    protected ListModel(Parcel in) {
        id = in.readInt();
        Inbound_shipment_number = in.readString();
        created_at = in.readString();
        Item_articles = in.readString();
        updated_at = in.readString();
        Items_amount = in.readInt();
        Supplier = in.readString();
        Client = in.readInt();
        Gate = in.readInt();
        status_id = in.readInt();
        Warehouse = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Inbound_shipment_number);
        dest.writeString(created_at);
        dest.writeString(Item_articles);
        dest.writeString(updated_at);
        dest.writeInt(Items_amount);
        dest.writeString(Supplier);
        dest.writeInt(Client);
        dest.writeInt(Gate);
        dest.writeInt(status_id);
        dest.writeString(Warehouse);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListModel> CREATOR = new Creator<ListModel>() {
        @Override
        public ListModel createFromParcel(Parcel in) {
            return new ListModel(in);
        }

        @Override
        public ListModel[] newArray(int size) {
            return new ListModel[size];
        }
    };
}
