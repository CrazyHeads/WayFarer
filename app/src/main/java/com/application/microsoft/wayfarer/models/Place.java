package com.application.microsoft.wayfarer.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by RAJULA on 28-01-2018.
 */

public class Place {
    String ID;
    String name;
    Double lat;
    Double lng;
    String imgURL;
    List<String> Type;
    Boolean isOpen;

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

    public List<String> getType() {

        return Type;
    }

    public void setType(List<String> type) {

        Type = type;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {

        isOpen = open;
    }



}
