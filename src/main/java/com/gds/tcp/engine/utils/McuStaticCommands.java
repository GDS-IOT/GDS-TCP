package com.gds.tcp.engine.utils;

/**
 * @author Sujith Ramanathan
 */
public class McuStaticCommands {

    private static final McuStaticCommands instance = new McuStaticCommands();

    private McuStaticCommands(){
    }

    public static McuStaticCommands getInstance(){
        return instance;
    }

    public byte[] getStaticMcuCommand(){
        byte[] data = new byte[20];
        data[0] = 20;
        data[1] = 11;
        data[2] = 37;
        data[3] = 4;
        data[4] = 28;
        data[5] = 1;
        data[6] = 11;
        data[7] = 1;
        data[8] = 0;
        data[9] = 0;
        data[10] = 0;
        data[11] = 0;
        data[12] = 0;
        data[13] = 0;
        data[14] = 0;
        data[15] = 0;
        data[16] = 0;
        data[17] = 0;
        data[18] = 0;
        data[19] = 0;


        return data;
    }
}