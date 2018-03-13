package com.fj.naufalprakoso.dicodingkamusmade.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NaufalPrakoso on 13/03/18.
 */

public class Kamus implements Parcelable {

    private int id;
    private String nama;
    private String keterangan;

    public Kamus() {
    }

    protected Kamus(Parcel in) {
        id = in.readInt();
        nama = in.readString();
        keterangan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nama);
        dest.writeString(keterangan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Kamus> CREATOR = new Creator<Kamus>() {
        @Override
        public Kamus createFromParcel(Parcel in) {
            return new Kamus(in);
        }

        @Override
        public Kamus[] newArray(int size) {
            return new Kamus[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public String toString() {
        return nama.toLowerCase();
    }
}
