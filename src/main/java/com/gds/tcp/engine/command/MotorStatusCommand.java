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

    private static final int PACKET_SIZE = 70;

    public static MotorStatusCommand getInstance() {
        return instance;
    }

    public byte[] getMotorCommand(JSONObject json) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuilder builder = new StringBuilder();
        byte[] data = new byte[PACKET_SIZE];

        baos.write(PACKET_SIZE);

        data = createMotorData(json, data, builder);
        fillBytesAsZero(baos);

        System.out.println(builder.toString());
//        byte[] data = baos.toByteArray();
        return data;
    }

    private byte[] createMotorData(JSONObject json, byte[] rawData, StringBuilder builder) {
        String packetSizeValue = String.valueOf(PACKET_SIZE);
        rawData[0] = (byte) packetSizeValue.charAt(0);
        rawData[1] = (byte) packetSizeValue.charAt(1);
        builder.append(rawData[0]).append(" - ").append(rawData[1]).append(" - ");
        try {
            int index = 2;
            String deviceId = (String) json.get(GDSConstants.DEVICE_ID_KEY);
            index = writeCharIntoByte(deviceId.split(GDSConstants.GDS_ID_DELIMITER), rawData, builder, index);

            // Device Type
            String deviceCategory = (String) json.get(GDSConstants.DEVICE_CATEGORY_KEY);
            index = writeCharIntoByte(deviceCategory.split(GDSConstants.GDS_ID_DELIMITER), rawData, builder, index);

            // Command Number
            rawData[index] = '0';
            index++;
            rawData[index] = '1';
            builder.append("0").append(" - ").append(1).append(" - ");
            index++;

            // Packet Type
            index = writeCharIntoByte(((String) json.get(GDSConstants.PACKET_TYPE_KEY)).split(""), rawData, builder, index);


            // Serial Data
            JSONArray serialDataArr = (JSONArray) json.get(GDSConstants.DEVICE_ACTION_SERIAL_DATA);
            int serialDataIndex;
            String serialDataValue;
            JSONObject indexAndValue;
            boolean isFirstElement = true;
            for (Object serialLocData : serialDataArr) {
                indexAndValue = (JSONObject) serialLocData;
                serialDataIndex = convertToInt((String) indexAndValue.get((GDSConstants.SERIAL_DATA_INDEX)));
                serialDataValue = (String) indexAndValue.get(GDSConstants.SERIAL_DATA_VALUE);

//                rawData[serialDataIndex] = (byte) convertToInt(serialDataValue);
//                index++;
                if(isFirstElement){
                    isFirstElement = false;
                }else if(!isFirstElement){
                    serialDataIndex = index + 1;
                }
                builder.append("serial_data_loc = ").append(serialDataIndex).append(" - ");
                index = writeCharIntoByte(serialDataValue.split(""), rawData, builder, serialDataIndex);
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

    private int writeCharIntoByte(String[] data, byte[] rawData, StringBuilder builder, int index) {
        int stringLength;
        for (String s : data) {
            stringLength = s.length();
            if (stringLength > 1) {
                index = appendChar(s, index, rawData, stringLength, builder);
            }else if (stringLength <= 1){
                rawData[index] = '0';
                index++;
                builder.append(0).append(" - ");
                rawData[index] = (byte) s.charAt(0);
                builder.append(rawData[index]).append(" - ");
                index++;
            }
        }
        return index;
    }

    private int appendChar(String value, int index, byte[] data, int stringLength, StringBuilder builder) {
        char ch;
        for (int i = 0; i < stringLength; i++) {
            ch = value.charAt(i);
            data[index] = (byte) ch;
            builder.append(ch).append(" - ");
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
