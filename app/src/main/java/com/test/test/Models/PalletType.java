package com.test.test.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class PalletType implements Parcelable {
    public int id;
    public String Pallet_Type;
    public PalletType(int id, String Pallet_Type) {
        this.id = id;
        this.Pallet_Type = Pallet_Type;
    }

    protected PalletType(Parcel in) {
        id = in.readInt();
        Pallet_Type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Pallet_Type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PalletType> CREATOR = new Creator<PalletType>() {
        @Override
        public PalletType createFromParcel(Parcel in) {
            return new PalletType(in);
        }

        @Override
        public PalletType[] newArray(int size) {
            return new PalletType[size];
        }
    };
}
