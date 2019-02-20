package com.example.ian.paypalpayment.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable
{
    private String id;
    private String user_id;
    private String category_id;
    private String name;
    private String description;
    private String model;
    private String price;
    private String group_size;
    private String image_path;

    public Item() {
    }

    public Item(String id, String user_id, String category_id, String name, String description, String model, String price, String group_size, String image_path) {
        this.id = id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.model = model;
        this.price = price;
        this.group_size = group_size;
        this.image_path = image_path;
    }

    protected Item(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        category_id = in.readString();
        name = in.readString();
        description = in.readString();
        model = in.readString();
        price = in.readString();
        group_size = in.readString();
        image_path = in.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGroup_size() {
        return group_size;
    }

    public void setGroup_size(String group_size) {
        this.group_size = group_size;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", category_id='" + category_id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", model='" + model + '\'' +
                ", price='" + price + '\'' +
                ", group_size='" + group_size + '\'' +
                ", image_path='" + image_path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(user_id);
        parcel.writeString(category_id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(model);
        parcel.writeString(price);
        parcel.writeString(group_size);
        parcel.writeString(image_path);
    }
}
