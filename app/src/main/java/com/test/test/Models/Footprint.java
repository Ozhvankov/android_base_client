package com.test.test.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Footprint implements Parcelable {
    public int id;
    public String footprint_name;
    public Footprint(int id, String footprint_name) {
        this.id = id;
        this.footprint_name = footprint_name;
    }

    protected Footprint(Parcel in) {
        id = in.readInt();
        footprint_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(footprint_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Footprint> CREATOR = new Creator<Footprint>() {
        @Override
        public Footprint createFromParcel(Parcel in) {
            return new Footprint(in);
        }

        @Override
        public Footprint[] newArray(int size) {
            return new Footprint[size];
        }
    };
}
