package com.test.test.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Lot implements Parcelable {
    public String id;
    public String Name;
    public int Unit_id;

    public static Lot getEmptyLot(){
        return new Lot("0","not find", 0);
    }

    public Lot(String id, String Name, int Unit_id) {
        this.id = id;
        this.Name = Name;
        this.Unit_id = Unit_id;
    }

    protected Lot(Parcel in) {
        id = in.readString();
        Name = in.readString();
        Unit_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(Name);
        dest.writeInt(Unit_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Lot> CREATOR = new Creator<Lot>() {
        @Override
        public Lot createFromParcel(Parcel in) {
            return new Lot(in);
        }

        @Override
        public Lot[] newArray(int size) {
            return new Lot[size];
        }
    };
}
