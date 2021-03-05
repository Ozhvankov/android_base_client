package com.test.test.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import com.test.test.R;

public class EditableItem implements Parcelable {
    public int Pallet_Type;
    public int id;
    public int wrh_zone;
    public int Item_inventory_status;
    public String Manufacturing_Date;
    public  String Transport_Equipment_Number;
    public   String Lot_number_batch;
    public int item_id;
    public String inbound_date;
    public int cell_id;
    public String cell_name;

    public int kg_current;
    public int box_current;

    public String Inbound_shipment_number;
    public String Supplier;
    public String Client;
    public int Item_No;
    public int kg_avaible;
    public int kg_reserved;
    public int box_avaible;
    public int box_reserved;

    public EditableItem(Parcel in) {
        id = in.readInt();
        Manufacturing_Date = in.readString();
        inbound_date = in.readString();
        kg_current = in.readInt();
        box_current = in.readInt();
        Item_inventory_status = in.readInt();
        Transport_Equipment_Number = in.readString();
        Lot_number_batch = in.readString();
        cell_id = in.readInt();
        cell_name = in.readString();
        item_id = in.readInt();
        wrh_zone = in.readInt();
        Pallet_Type = in.readInt();
        Supplier= in.readString();;
        Client= in.readString();;
        Item_No= in.readInt();
        kg_avaible= in.readInt();
        kg_reserved= in.readInt();
        box_avaible= in.readInt();
        box_reserved= in.readInt();
    }

    public EditableItem() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Manufacturing_Date);
        dest.writeString(inbound_date);
        dest.writeInt(kg_current);
        dest.writeInt(box_current);
        dest.writeInt(Item_inventory_status);
        dest.writeString(Transport_Equipment_Number);
        dest.writeString(Lot_number_batch);
        dest.writeInt(cell_id);
        dest.writeString(cell_name);
        dest.writeInt(item_id);
        dest.writeInt(wrh_zone);
        dest.writeInt(Pallet_Type);

        dest.writeString(Supplier);
        dest.writeString(Client);
        dest.writeInt(Item_No);
        dest.writeInt(kg_avaible);
        dest.writeInt(kg_reserved);
        dest.writeInt(box_avaible);
        dest.writeInt(box_reserved);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EditableItem> CREATOR = new Creator<EditableItem>() {
        @Override
        public EditableItem createFromParcel(Parcel in) {
            return new EditableItem(in);
        }

        @Override
        public EditableItem[] newArray(int size) {
            return new EditableItem[size];
        }
    };
}
