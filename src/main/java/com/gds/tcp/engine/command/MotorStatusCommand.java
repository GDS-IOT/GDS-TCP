package com.gds.tcp.engine.command;

import com.gds.tcp.engine.constants.GDSConstants;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;
import java.sql.SQLOutput;

/**
 * @author Sujith Ramanathan
 */
public class MotorStatusCommand {

    private static final MotorStatusCommand instance = new MotorStatusCommand();

    private static final Logger LOGGER = Logger.getLogger(MotorStatusCommand.class);

    private static final int PACKET_SIZE = 35;

    public static MotorStatusCommand getInstance() {
        return instance;
    }

    public byte[] getMotorCommand(JSONObject json) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder builder = new StringBuilder();
        byte[] data = new byte[PACKET_SIZE];

        baos.write(PACKET_SIZE);
        builder.append(PACKET_SIZE).append(" - ");

        data = createMotorData(json, data, builder);
        fillBytesAsZero(baos);

        System.out.println(builder.toString());
//        byte[] data = baos.toByteArray();
        return data;
    }

    private byte[] createMotorData(JSONObject json, byte[] rawData, StringBuilder builder) {
        rawData[0] = PACKET_SIZE;
        try {
            int index = 1;
            String deviceId = (String) json.get(GDSConstants.DEVICE_ID_KEY);
            index = writeStringIntoByte(deviceId.split(GDSConstants.GDS_ID_DELIMITER), rawData, builder, index);

            // Device Type
            String deviceCategory = (String) json.get(GDSConstants.DEVICE_CATEGORY_KEY);
            index = writeStringIntoByte(deviceCategory.split(GDSConstants.GDS_ID_DELIMITER), rawData, builder, index);

            // Command Number
            rawData[index] = 1;
            builder.append("1").append(" - ");
            index++;

            // Packet Type
            int packetType = convertToInt((String) json.get(GDSConstants.PACKET_TYPE_KEY));
            rawData[index] = (byte) packetType;
            index++;
            builder.append(packetType).append(" - ");

            // Serial Data
            JSONArray serialDataArr = (JSONArray) json.get(GDSConstants.DEVICE_ACTION_SERIAL_DATA);
            int serialDataIndex;
            String serialDataValue;
            JSONObject indexAndValue;
            for (Object serialLocData : serialDataArr) {
                indexAndValue = (JSONObject) serialLocData;
                serialDataIndex = convertToInt((String) indexAndValue.get((GDSConstants.SERIAL_DATA_INDEX)));
                serialDataValue = (String) indexAndValue.get(GDSConstants.SERIAL_DATA_VALUE);

                rawData[serialDataIndex] = (byte) convertToInt(serialDataValue);
                index++;

                builder.append("serial_data_loc = ").append(serialDataIndex).append(" ");
                builder.append(serialDataValue).append(" - ");
            }
            LOGGER.debug("Payload sending to device ".concat(builder.toString()));
        } catch (Exception e) {
            LOGGER.error("Generic Error occurred while writing into baos ", e);
        }
        return rawData;
    }

    private int writeStringIntoByte(String[] data, byte[] rawData, StringBuilder builder, int index) {
        for (String s : data) {
            int val = convertToInt(s);
            rawData[index] = (byte) val;
            builder.append(val).append(" - ");
            index++;
        }
        return index;
    }

    private int convertToInt(String data) {
        return Integer.parseInt(data);
    }

    private void fillBytesAsZero(ByteArrayOutputStream baos) {
        byte[] data = baos.toByteArray();
        for (int i = data.length; i < PACKET_SIZE; i++) {
            baos.write(0);
        }
    }

//    public static void main(String[] args) throws Exception {
//
//        String msg = "{\n" +
//                "\"fac_gateway_device_id\":\"0-1-34-76\",\n" +
//                "\"fac_mcu_device_id\":\"1-36-76\",\n" +
//                "\"fac_mcu_device_category\":\"28\",\n" +
//                "\"id_event_definition\":\"85\",\n" +
//                "\"packet_type\":\"17\",\n" +
//                "\"device_action_serial_data\":\n" +
//                "[\n" +
//                "{\n" +
//                "\"serialdata_location\":\"7\",\n" +
//                "\"serialdata_value\":\"1\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"serialdata_location\":\"8\",\n" +
//                "\"serialdata_value\":\"85\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"serialdata_location\":\"9\",\n" +
//                "\"serialdata_value\":\"21\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"serialdata_location\":\"10\",\n" +
//                "\"serialdata_value\":\"3\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"serialdata_location\":\"11\",\n" +
//                "\"serialdata_value\":\"0\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"serialdata_location\":\"12\",\n" +
//                "\"serialdata_value\":\"0\"\n" +
//                "},\n" +
//                "{\n" +
//                "\"serialdata_location\":\"13\",\n" +
//                "\"serialdata_value\":\"0\"\n" +
//                "}\n" +
//                "]\n" +
//                "}";
//        JSONParser parser = new JSONParser();
//
//        byte[] data = getInstance().getMotorCommand((JSONObject) parser.parse(msg));
//        for (byte b : data) {
//            System.out.println(b);
//        }
//    }
}
