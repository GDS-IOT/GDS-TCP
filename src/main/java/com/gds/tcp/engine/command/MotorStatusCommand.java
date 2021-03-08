package com.gds.tcp.engine.command;

import com.gds.tcp.engine.constants.GDSConstants;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;

/**
 * @author Sujith Ramanathan
 */
public class MotorStatusCommand {

    private static final MotorStatusCommand instance = new MotorStatusCommand();

    private static final Logger LOGGER = Logger.getLogger(MotorStatusCommand.class);

    private static final int MOTOR_ON_OFF_PACKET_SIZE = 20;

    public static MotorStatusCommand getInstance() {
        return instance;
    }

    public byte[] getMotorCommand(JSONObject json) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder builder = new StringBuilder();

        baos.write(MOTOR_ON_OFF_PACKET_SIZE);
        builder.append(MOTOR_ON_OFF_PACKET_SIZE).append(" - ");

        createMotorData(json, baos, builder);
        fillBytesAsZero(baos);

        byte[] data = baos.toByteArray();
        return data;
    }

    private void createMotorData(JSONObject json, ByteArrayOutputStream baos, StringBuilder builder) {
        try {
            String deviceId = (String) json.get(GDSConstants.DEVICE_ID_KEY);
            writeStringIntoByte(deviceId.split(GDSConstants.GDS_ID_DELIMITER), baos, builder);

            // Device Type
            String deviceCategory = (String) json.get(GDSConstants.DEVICE_CATEGORY_KEY);
            writeStringIntoByte(deviceCategory.split(GDSConstants.GDS_ID_DELIMITER), baos, builder);

            // Command Number
            baos.write(1);
            builder.append("1").append(" - ");

            // Packet Type
            int packetType = convertToInt((String) json.get(GDSConstants.PACKET_TYPE_KEY));
            baos.write(packetType);
            builder.append(packetType).append(" - ");

            // Serial Data
            JSONArray serialData = (JSONArray) json.get(GDSConstants.DEVICE_ACTION_SERIAL_DATA);
            String value;
            for (Object serialLocData : serialData) {
                value = (String) ((JSONObject) serialLocData).get(GDSConstants.SERIAL_DATA_VALUE);
                baos.write(convertToInt(value));
                builder.append(value).append(" - ");
            }
            LOGGER.debug("Payload sending to device ".concat(builder.toString()));
        } catch (Exception e) {
            LOGGER.error("Generic Error occurred while writing into baos ", e);
        }
    }

    private void writeStringIntoByte(String[] data, ByteArrayOutputStream baos, StringBuilder builder) {
        for (String s : data) {
            int val = convertToInt(s);
            baos.write(val);
            builder.append(val).append(" - ");
        }
    }

    private int convertToInt(String data) {
        return Integer.parseInt(data);
    }

    private void fillBytesAsZero(ByteArrayOutputStream baos) {
        byte []data = baos.toByteArray();
        for(int i=data.length; i < MOTOR_ON_OFF_PACKET_SIZE; i++){
            baos.write(0);
        }
    }

//    public static void main(String[] args) throws Exception {
//        String msg = "{\n" +
//                "\"fac_gateway_device_id\":\"0-1-13-74\",\n" +
//                "\"fac_mcu_device_id\":\"1-13-75\",\n" +
//                "\"fac_mcu_device_category\":\"28\",\n" +
//                "\"id_event_defination\":\"70\",\n" +
//                "\"packet_type\":\"3\",\n" +
//                "\"device_action_serial_data\":\n" +
//                "[\n" +
//                "{\n" +
//                "\"serialdata_location\":\"8\",\n" +
//                "\"serialdata_value\":\"1\"\n" +
//                "}\n" +
//                "]\n" +
//                "}";
//        JSONParser parser = new JSONParser();
//
//        getInstance().getMotorCommand((JSONObject) parser.parse(msg));
//    }
}
