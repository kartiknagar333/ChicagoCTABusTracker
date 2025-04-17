package com.example.ctabustracker.Model;

import static androidx.core.graphics.ColorUtils.calculateLuminance;

import android.graphics.Color;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Routes implements Serializable {

    @JsonProperty("rt")
    private final String rt;

    @JsonProperty("rtnm")
    private final String rtnm;

    @JsonProperty("rtclr")
    private final String rtclr;

    @JsonProperty("rtdd")
    private final String rtdd;

    public Routes(final String rt, final String rtnm, final String rtclr, final String rtdd) {
        this.rt = rt;
        this.rtnm = rtnm;
        this.rtclr = rtclr;
        this.rtdd = rtdd;

    }

    public String getRt() {
        return rt;
    }

    public String getRtnm() {
        return rtnm;
    }

    public String getRtclr() {
        return rtclr;
    }

    public int backgroundColor() {
         return Color.parseColor(rtclr);
    }

    public int textColor() {
        double luminance = calculateLuminance(Color.parseColor(rtclr));

        return (luminance < 0.25) ? Color.WHITE : Color.BLACK;
    }

    public String getRtdd() {
        return rtdd;
    }

}

