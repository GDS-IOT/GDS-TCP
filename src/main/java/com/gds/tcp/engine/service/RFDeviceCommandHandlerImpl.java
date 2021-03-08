package com.gds.tcp.engine.service;

import com.gds.tcp.engine.constants.GDSConstants;
import com.gds.tcp.engine.netty.GDSNettyChannelGroup;
import com.gds.tcp.engine.utils.McuCommandGenerator;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;

/**
 * @author Sujith Ramanathan
 */
public class RFDeviceCommandHandlerImpl implements GDSHandler {

    private static final Logger LOGGER = Logger.getLogger(RFDeviceCommandHandlerImpl.class);

    private static final RFDeviceCommandHandlerImpl instance = new RFDeviceCommandHandlerImpl();

    private final McuCommandGenerator commandGenerator = McuCommandGenerator.getInstance();

    private final GDSNettyChannelGroup nettChannelGrp = GDSNettyChannelGroup.getInstance();

    private static final JSONParser parser = new JSONParser();

    private RFDeviceCommandHandlerImpl(){}

    public static RFDeviceCommandHandlerImpl getInstance(){
        return instance;
    }

    @Override
    public void handleNext(byte[] data, Channel channel) {
        JSONObject payload = getJson(data);
        if (null == payload) {
            LOGGER.debug("Invalid payload / Payload is null");
            return;
        }
        byte[] formattedData = commandGenerator.getRespectiveCommand(payload);
        sendToRespectiveDevice(payload, formattedData);
    }

    private void sendToRespectiveDevice(JSONObject payload, byte[] data){
        if (null != data) {
            String rfid = (String)payload.get(GDSConstants.RFID_KEY);
            Channel mcuChannel = nettChannelGrp.getChannelByRfId(rfid);
            mcuChannel.writeAndFlush(data);
            LOGGER.debug("Data sent to RF Successfully");
        } else {
            LOGGER.debug("Data Not sent to RF, Failed");
        }
    }

    private JSONObject getJson(byte[] data) {
        // Eliminating 1st element as it is an identifier
        byte[] payload = Arrays.copyOfRange(data, 1, data.length);
        String payloadString = new String(payload);
        LOGGER.debug("Payload :: ".concat(payloadString));
        try {
            JSONObject rfData = (JSONObject) parser.parse(payloadString);
            return rfData;
        } catch (ParseException pe) {
            LOGGER.error("Error occurred while parsing JSON ", pe);
            return null;
        } catch (Exception e) {
            LOGGER.error("Generic Error occurred while parsing JSON ", e);
            return null;
        }
    }
}