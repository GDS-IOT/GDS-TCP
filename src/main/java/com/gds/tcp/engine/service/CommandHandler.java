package com.gds.tcp.engine.service;

import org.apache.log4j.Logger;

public class CommandHandler {

    private static final Logger LOGGER = Logger.getLogger(CommandHandler.class);


    public byte[] getDevice(byte []reqFromDevice){
        byte []req = new byte[8];
        req[0] = (byte)0x0A;
        req[1] = (byte)0x01;
        req[2] = (byte)0x06;
        req[3] = (byte)0x00;
        req[4] = (byte)0x00;
        req[5] = (byte)0x01;
        req[6] = (byte)0x03;
        req[7] = (byte)0x11;
        if(new String(req).equals(reqFromDevice)) {
            LOGGER.debug("Sending correct response");
            byte []res = new byte[8];
            res[0] = (byte)0x08;
            res[1] = (byte)0x01;
            res[2] = (byte)0x06;
            res[3] = (byte)0x00;
            res[4] = (byte)0x00;
            res[5] = (byte)0x01;
            res[6] = (byte)0x11;
            res[7] = (byte)0x01;
            return res;
        }
        LOGGER.debug("Sending error response");

        return null;
    }

    public byte[] sendDeviceResponse(){
        byte []res = new byte[10];
        res[0] = (byte)0x0A;
        res[1] = (byte)0xff;
        res[2] = (byte)0xff;
        res[3] = (byte)0xff;
        res[4] = (byte)0xff;
        res[5] = (byte)0x01;
        res[6] = (byte)0x11;
        res[7] = (byte)0x01;
        res[8] = (byte)0x00;
        res[9] = (byte)0x00;
        return res;
    }

    public byte[] sendDeviceResponse2(){
        byte []res = new byte[10];
        res[0] = (byte)0x0A;
        res[1] = (byte)0x01;
        res[2] = (byte)0x06;
        res[3] = (byte)0x00;
        res[4] = (byte)0x00;
        res[5] = (byte)0x01;
        res[6] = (byte)0x11;
        res[7] = (byte)0x01;
        res[8] = (byte)0x00;
        res[9] = (byte)0x00;
        return res;
    }

    public byte[] sendDeviceResponse3(){
        byte []res = new byte[10];
        res[0] = (byte)0x0A;
        res[1] = (byte)0x01;
        res[2] = (byte)0x05;
        res[3] = (byte)0x00;
        res[4] = (byte)0x00;
        res[5] = (byte)0x01;
        res[6] = (byte)0x11;
        res[7] = (byte)0x01;
        res[8] = (byte)0x00;
        res[9] = (byte)0x00;
        return res;
    }
}
