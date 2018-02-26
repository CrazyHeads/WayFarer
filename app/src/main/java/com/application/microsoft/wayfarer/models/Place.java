package com.application.microsoft.wayfarer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class Place implements Parcelable{
    String ID;
    String name;
    Double lat;
    Double lng;
    String imgURL;
    List<String> type;
    Boolean isOpen;
    Boolean isSelected;
    String city;

    public Place() {

    }

    protected Place(Parcel in) {
        ID = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
        imgURL = in.readString();
        type = in.createStringArrayList();
        byte tmpIsOpen = in.readByte();
        isOpen = tmpIsOpen == 0 ? null : tmpIsOpen == 1;
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
        city = in.readString();
        description = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
   
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

   

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    String description;

    public Double getLat() {

        return lat;
    }

    public void setLat(Double lat) {

        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {

        this.lng = lng;
    }



    public String getID() {

        return ID;
    }

    public void setID(String ID) {

        this.ID = ID;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getImgURL() {

        return imgURL;
    }

    public void setImgURL(String imgURL) {

        this.imgURL = imgURL;
    }

    public List<String> gettype() {

        return type;
    }

    public void settype(List<String> type) {

        type = type;
    }

    public Boolean getOpen() {

        return isOpen;
    }

    public void setOpen(Boolean open) {

        isOpen = open;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(name);
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lng);
        }
        dest.writeString(imgURL);
        dest.writeStringList(type);
        dest.writeByte((byte) (isOpen == null ? 0 : isOpen ? 1 : 2));
        dest.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
        dest.writeString(city);
        dest.writeString(description);
    }
}
