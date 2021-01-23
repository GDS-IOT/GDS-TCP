package com.gds.tcp.engine.domain;

public class GDSData {

    private String ts;
    private byte[] gdsData;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public byte[] getGdsData() {
        return gdsData;
    }

    public void setGdsData(byte[] gdsData) {
        this.gdsData = gdsData;
    }
}
