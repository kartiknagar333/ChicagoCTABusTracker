package com.example.ctabustracker.Model;

import static androidx.core.graphics.ColorUtils.calculateLuminance;

import android.graphics.Color;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Stops implements Serializable {

    @JsonProperty("stpid")
    private final String stpid;

    @JsonProperty("stpnm")
    private final String stpnm;

    @JsonProperty("lat")
    private final double lat;

    @JsonProperty("lon")
    private final double lon;

    public Stops(String stpid, String stpnm, double lat, double lon) {
        this.stpid = stpid;
        this.stpnm = stpnm;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStpid() {
        return stpid;
    }

    public String getStpnm() {
        return stpnm;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}

