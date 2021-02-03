package com.test.test.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Item implements Parcelable {
    public String id;
    public String Item_No;
    public String Item_article;

    public Item(String id,
            String Item_No,
            String Item_article) {
        this.id = id;
        this.Item_No = Item_No;
        this.Item_article = Item_article;
    }

    protected Item(Parcel in) {
        id = in.readString();
        Item_No = in.readString();
        Item_article = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(Item_No);
        dest.writeString(Item_article);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
