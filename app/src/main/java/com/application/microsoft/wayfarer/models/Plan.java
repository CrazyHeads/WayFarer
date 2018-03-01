package com.application.microsoft.wayfarer.models;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by RAJULA on 01-03-2018.
 */

public class Plan extends ArrayList<Parcelable> {
    ArrayList<Place> planedPlaces;
    String city;
    Date madeOn;

    public ArrayList<Place> getPlanedPlaces() {
        return planedPlaces;
    }

    public void setPlanedPlaces(ArrayList<Place> planedPlaces) {
        this.planedPlaces = planedPlaces;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getMadeOn() {
        return madeOn;
    }

    public void setMadeOn(Date madeOn) {
        this.madeOn = madeOn;
    }
}
