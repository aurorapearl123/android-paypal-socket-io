package com.example.ian.paypalpayment.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Client implements Parcelable
{
    private String id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String phone;
    private String email;
    private String profile;

    public Client() {
    }

    public Client(String id, String first_name, String middle_name, String last_name, String phone, String email, String profile) {
        this.id = id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.profile = profile;
    }

    public Client(Parcel in) {
        this.id = in.readString();
        this.first_name = in.readString();
        this.middle_name = in.readString();
        this.last_name = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.profile = in.readString();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.first_name);
        parcel.writeString(this.middle_name);
        parcel.writeString(this.last_name);
        parcel.writeString(this.email);
        parcel.writeString(this.phone);
        parcel.writeString(this.profile);
    }


}
