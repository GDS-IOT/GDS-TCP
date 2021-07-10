package com.gds.tcp.engine.converter;

import org.apache.log4j.Logger;

/**
 * @author Sujith Ramanathan
 */
public class AsciiToByteConverter {

    private static final AsciiToByteConverter instance = new AsciiToByteConverter();

    private static final Logger LOGGER = Logger.getLogger(AsciiToByteConverter.class);

    private static final String EMPTY_STRING = "";

    private static final StringBuilder builder = new StringBuilder();

    private AsciiToByteConverter() {
    }

    public static AsciiToByteConverter getInstance() {
        return instance;
    }

    public byte[] convertToAnalyticsData(byte[] rawData) {
        byte[] data = new byte[35];

        LOGGER.debug(builder.append("RawData -- ").append(new String(rawData)).toString());
        builder.setLength(0);

        // Total number of bytes
        data[0]=35;

        int idx = 1;
        // Setting systemId
        idx = setData(rawData, data, idx, 2, 9);

        // Setting deviceId
        idx = setData(rawData, data, idx, 10, 15);

        // Setting deviceType
        idx = setData(rawData, data, idx, 16, 17);

        // Setting Origin RSSI
        idx = setData(rawData, data, idx, 18, 19);

        // Setting Origin Network Level
        idx = setData(rawData, data, idx, 20, 21);

        // Setting Origin Hop Counter
        idx = setData(rawData, data, idx, 22, 23);

        // Setting Origin Message Counter
        idx = setData(rawData, data, idx, 24, 27);

        // Setting Origin Latency Counter
        idx = setData(rawData, data, idx, 28, 31);

        // Setting Packet Type
        idx = setData(rawData, data, idx, 32, 33);

        // Setting Message Type
        idx = setData(rawData, data, idx, 34,35);

        // Event Id
        idx = setData(rawData, data, idx, 36,37);

        // Remaining data
        setData(rawData, data, idx, 38,68);

        for (byte b : data) {
            builder.append((int) b).append(" - ");
        }
        LOGGER.debug("AsciiToByte Data :: ".concat(builder.toString()));
        builder.setLength(0);

        return data;
    }

    private int setData(byte[] rawData, byte[] actualData, int idx, int startIndex, int endIndex) {
        String value = "";
        int intValue = 0;
        for (int i = startIndex; i <= endIndex; i += 2) {
            value = (String.valueOf((char) rawData[i]).concat(String.valueOf((char) rawData[i + 1]))).trim();
            if (!EMPTY_STRING.equals(value)) {
                intValue = Integer.parseInt(value);
                actualData[idx] = (byte) intValue;
                idx++;
            }
        }
        return idx;
    }
}
