package com.example.ctabustracker.Model;

import static androidx.core.graphics.ColorUtils.calculateLuminance;

import android.graphics.Color;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PredictionBus implements Serializable {

    @JsonProperty("tmstmp")
    private final String tmstmp;

    @JsonProperty("typ")
    private final String typ;

    @JsonProperty("stpnm")
    private final String stpnm;

    @JsonProperty("stpid")
    private final String stpid;

    @JsonProperty("vid")
    private final String vid;

    @JsonProperty("dstp")
    private final int dstp;

    @JsonProperty("rt")
    private final String rt;

    @JsonProperty("rtdd")
    private final String rtdd;

    @JsonProperty("rtdir")
    private final String rtdir;

    @JsonProperty("des")
    private final String des;

    @JsonProperty("prdtm")
    private final String prdtm;

    @JsonProperty("tablockid")
    private final String tablockid;

    @JsonProperty("tatripid")
    private final String tatripid;

    @JsonProperty("origtatripno")
    private final String origtatripno;

    @JsonProperty("dly")
    private final boolean dly;

    @JsonProperty("prdctdn")
    private final String prdctdn;

    @JsonProperty("zone")
    private final String zone;

    public PredictionBus(String tmstmp, String typ, String stpnm, String stpid, String vid, int dstp, String rt, String rtdd, String rtdir, String des, String prdtm, String tablockid, String tatripid, String origtatripno, boolean dly, String prdctdn, String zone) {
        this.tmstmp = tmstmp;
        this.typ = typ;
        this.stpnm = stpnm;
        this.stpid = stpid;
        this.vid = vid;
        this.dstp = dstp;
        this.rt = rt;
        this.rtdd = rtdd;
        this.rtdir = rtdir;
        this.des = des;
        this.prdtm = prdtm;
        this.tablockid = tablockid;
        this.tatripid = tatripid;
        this.origtatripno = origtatripno;
        this.dly = dly;
        this.prdctdn = prdctdn;
        this.zone = zone;
    }

    public String getTmstmp() {
        return tmstmp;
    }

    public String getTyp() {
        return typ;
    }

    public String getStpnm() {
        return stpnm;
    }

    public String getStpid() {
        return stpid;
    }

    public String getVid() {
        return vid;
    }

    public int getDstp() {
        return dstp;
    }

    public String getRt() {
        return rt;
    }

    public String getRtdd() {
        return rtdd;
    }

    public String getRtdir() {
        return rtdir;
    }

    public String getDes() {
        return des;
    }

    public String getPrdtm() {
        return prdtm;
    }

    public String getTablockid() {
        return tablockid;
    }

    public String getTatripid() {
        return tatripid;
    }

    public String getOrigtatripno() {
        return origtatripno;
    }

    public boolean isDly() {
        return dly;
    }

    public String getPrdctdn() {
        return prdctdn;
    }

    public String getZone() {
        return zone;
    }
}

