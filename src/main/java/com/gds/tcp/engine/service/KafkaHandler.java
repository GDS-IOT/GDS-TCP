package com.gds.tcp.engine.service;

import com.gds.domain.GDSData;
import com.gds.tcp.engine.constants.GDSConstants;
import com.gds.tcp.engine.utils.GDSSerializer;
import com.gds.tcp.engine.utils.GDSUtils;
import io.netty.channel.Channel;
import org.apache.kafka.clients.Metadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaHandler implements GDSHandler {

    private static final GDSHandler instance = new KafkaHandler();

    private static final Logger LOGGER = Logger.getLogger(KafkaHandler.class);

    private final StringBuilder stringBuilder = new StringBuilder();

    private GDSUtils gdsUtils;

    private GDSHandler rfIdHandler;

    private Producer<String, ByteArraySerializer> producer;

    private KafkaHandler() {
        gdsUtils = GDSUtils.getInstance();
        rfIdHandler = RFDeviceMappingHandlerImpl.getInstance();
        initKafkaProducer();
    }

    public static GDSHandler getInstance() {
        return instance;
    }

    @Override
    public void handleNext(byte []data, Channel channel) {
        LOGGER.debug("Sending message to Kafka");
        String systemId = getSystemId(data);
        GDSData gdsData = new GDSData();
        gdsData.setGdsData(data);
        gdsData.setTs(getCurrentTs());
        LOGGER.debug("systemId ".concat(systemId));
        ProducerRecord record = new ProducerRecord(
                gdsUtils.getGDSProperty(GDSConstants.KAFKA_TOPIC_NAME),
                systemId,
                gdsData
                );
        Future<Metadata> futureMedatata = this.producer.send(record);
        while(!futureMedatata.isDone()){}
        rfIdHandler.handleNext(data, channel);
    }

    private void initKafkaProducer() {
        this.producer = new KafkaProducer<String, ByteArraySerializer>(getKafkaProps());
    }

    private Properties getKafkaProps() {
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", gdsUtils.getGDSProperty(GDSConstants.KAFKA_BROKER_URL));
        kafkaProps.put("acks", "all");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // Serialization of keys
        kafkaProps.put("value.serializer", GDSSerializer.class.getName()); // Serialization of values
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

    private String getCurrentTs(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
