package com.gds.tcp.engine.service;

import com.gds.tcp.engine.constants.GDSConstants;
import com.gds.tcp.engine.netty.GDSNettyChannelGroup;
import io.netty.channel.Channel;

/**
 * @author Sujith Ramanathan
 */
public class RFDeviceMappingHandlerImpl implements GDSHandler {

    public static final RFDeviceMappingHandlerImpl instance = new RFDeviceMappingHandlerImpl();

    private final GDSNettyChannelGroup nettyChannelGroup = GDSNettyChannelGroup.getInstance();

    public static RFDeviceMappingHandlerImpl getInstance(){
        return instance;
    }

    @Override
    public void handleNext(byte []data, Channel channel) {
        if(data[GDSConstants.PACKET_TYPE_IDX] == GDSConstants.HEART_BEAT_EVENT) {
            String rfId = getString(data, GDSConstants.RFID_START_IDX, GDSConstants.RFID_END_IDX, GDSConstants.GDS_ID_DELIMITER);
            nettyChannelGroup.mapRFAndChannel(rfId, channel);
        }
    }

    public String getString(byte[] data, int startIndex, int endIndex, String delimiter) {
        String value = "";
        for (int i = startIndex; i <= endIndex; i++) {
            value = value.concat(delimiter).concat(String.valueOf((int) data[i]));
        }
        if ("".equals(value)) {
            for (int i = startIndex; i <= endIndex; i++) {
                value = value.concat(delimiter).concat("0");
            }
        }
        return value.replaceFirst(delimiter, "");
    }
}
