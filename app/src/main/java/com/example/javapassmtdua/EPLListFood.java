package com.example.javapassmtdua;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class EPLListFood implements Parcelable {
    private String strNumber;
    private String strName;
    private String strImage;
    private String strDesc;

    protected EPLListFood(Parcel in) {
        strNumber = in.readString();
        strName = in.readString();
        strImage = in.readString();
        strDesc = in.readString();
    }

    EPLListFood(){};

    public static final Creator<EPLListFood> CREATOR = new Creator<EPLListFood>() {
        @Override
        public EPLListFood createFromParcel(Parcel in) {
            return new EPLListFood(in);
        }

        @Override
        public EPLListFood[] newArray(int size) {
            return new EPLListFood[size];
        }
    };



    public String getStrNumber() {
        return strNumber;
    }

    public void setStrNumber(String strNumber) {
        this.strNumber = strNumber;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrImage() {
        return strImage;
    }

    public void setStrImage(String strImage) {
        this.strImage = strImage;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(strNumber);
        dest.writeString(strName);
        dest.writeString(strImage);
        dest.writeString(strDesc);
    }
}
