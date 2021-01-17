package com.gds.tcp.engine.service;

import com.gds.tcp.engine.constants.GDSConstants;
import com.gds.tcp.engine.utils.GDSUtils;
import org.apache.kafka.clients.Metadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaHandler implements GDSHandler {

    private static final GDSHandler instance = new KafkaHandler();

    private static final Logger LOGGER = Logger.getLogger(KafkaHandler.class);

    private final StringBuilder stringBuilder = new StringBuilder();

    private GDSUtils gdsUtils;
    private Producer<String, ByteArraySerializer> producer;

    private KafkaHandler() {
        gdsUtils = GDSUtils.getInstance();
        initKafkaProducer();
    }

    public static GDSHandler getInstance() {
        return instance;
    }

    @Override
    public void handleNext(Object rawData) {
        byte[] data = (byte[]) rawData;
        String systemId = getSystemId(data);
        LOGGER.debug("systemId ".concat(systemId));
        ProducerRecord record = new ProducerRecord(
                gdsUtils.getGDSProperty(GDSConstants.KAFKA_TOPIC_NAME),
                systemId,
                rawData
                );
        Future<Metadata> futureMedatata = this.producer.send(record);
        while(!futureMedatata.isDone()){}
    }

    private void initKafkaProducer() {
        this.producer = new KafkaProducer<String, ByteArraySerializer>(getKafkaProps());
    }

    private Properties getKafkaProps() {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", gdsUtils.getGDSProperty(GDSConstants.KAFKA_BROKER_URL));
        kafkaProps.put("acks", "all");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // Serialization of keys
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer"); // Serialization of values
        return kafkaProps;
    }

    private String getSystemId(byte[] data) {
        for (int i = 1, value = 0; i <= 4; i++) {
            value = data[i];
            if (0 != value){
                stringBuilder.append(value);
            }
        }
        String systemId = stringBuilder.toString();
        stringBuilder.setLength(0);
        return systemId;
    }
}
