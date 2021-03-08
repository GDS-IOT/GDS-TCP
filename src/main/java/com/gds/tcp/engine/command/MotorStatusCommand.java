package com.gds.tcp.engine.command;

import com.gds.tcp.engine.constants.GDSConstants;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Sujith Ramanathan
 */
public class MotorStatusCommand {

    private static final MotorStatusCommand instance = new MotorStatusCommand();

    private static final Logger LOGGER = Logger.getLogger(MotorStatusCommand.class);

    public static MotorStatusCommand getInstance() {
        return instance;
    }

    public byte[] getMotorCommand(JSONObject json) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(20);
        createMotorData(json, baos);
        byte []data = baos.toByteArray();
        LOGGER.debug("ByteArray payload ".concat(new String(data)));
        return data;
    }

    private void createMotorData(JSONObject json, ByteArrayOutputStream baos) {
        try {
            String deviceId = (String) json.get(GDSConstants.DEVICE_ID_KEY);
            baos.write(deviceId.getBytes());

            // Device Type
            baos.write(Integer.parseInt((String) json.get(GDSConstants.DEVICE_CATEGORY_KEY)));

            // Command Number
            baos.write(1);

            // Packet Type
            baos.write(Integer.parseInt((String) json.get(GDSConstants.PACKET_TYPE_KEY)));

            // Serial Data
            JSONArray serialData = (JSONArray) json.get(GDSConstants.DEVICE_ACTION_SERIAL_DATA);
            String value;
            for (Object serialLocData : serialData) {
                value = (String)((JSONObject) serialLocData).get(GDSConstants.SERIAL_DATA_VALUE);
                baos.write(Integer.parseInt(value));
            }

        } catch (IOException ie) {
            LOGGER.error("Error occurred while writing into baos ", ie);
        } catch (Exception e) {
            LOGGER.error("Generic Error occurred while writing into baos ", e);
        }

    }

}
