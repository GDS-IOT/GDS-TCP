package com.gds.tcp.engine.constants;

public class GDSConstants {

    public static final String KAFKA_BROKER_URL = "kafka.broker.url";

    public static final String KAFKA_TOPIC_NAME = "kafka.topic.name";

    public static final String GDS_ID_DELIMITER = "-";

    public static final int HEART_BEAT_EVENT = 2;

    public static final int PACKET_TYPE_IDX = 16;

    public static final int RFID_START_IDX = 1;

    public static final int RFID_END_IDX = 4;



    // Payload Keys
    public static final String RFID_KEY = "fac_gateway_device_id";

    public static final String DEVICE_ID_KEY = "fac_mcu_device_id";

    public static final String DEVICE_CATEGORY_KEY = "fac_mcu_device_category";

    public static final String EVENT_ID_KEY = "id_event_definition";

    public static final String PACKET_TYPE_KEY = "packet_type";

    public static final String DEVICE_ACTION_SERIAL_DATA = "device_action_serial_data";

    public static final String SERIAL_DATA_LOC = "serialdata_location";

    public static final String SERIAL_DATA_VALUE = "serialdata_value";


    // Event Id's
    public static final String MOTOR_STATUS_EVENT_ID = "70";

}
