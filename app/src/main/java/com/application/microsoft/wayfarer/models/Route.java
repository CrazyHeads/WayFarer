package com.application.microsoft.wayfarer.models;

import java.util.ArrayList;

/**
 * Created by RAJULA on 26-02-2018.
 */

public class Route {
    String source;
    String destination;
    ArrayList<Transit> transitInfo;

    public ArrayList<Transit> getTransitInfo() {
        return transitInfo;
    }

    public void setTransitInfo(ArrayList<Transit> transitInfo) {
        this.transitInfo = transitInfo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }



}
