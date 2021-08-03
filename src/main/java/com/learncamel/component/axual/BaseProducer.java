package com.learncamel.component.axual;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.axual.client.AxualClient;
import io.axual.client.config.BaseProducerConfig;
import io.axual.client.config.GenericAvroProducerConfig;
import io.axual.client.config.ProducerConfig;
import io.axual.client.config.SpecificAvroProducerConfig;
import io.axual.client.producer.ProducedMessage;
import io.axual.client.producer.Producer;
import io.axual.client.producer.ProducerMessage;
import org.apache.avro.specific.SpecificRecord;

import java.io.IOException;
import java.util.concurrent.Future;


public class BaseProducer implements AutoCloseable {
    private final Producer producer;
    private String stream;

    public BaseProducer(
            final AxualClient axualClient
            , final BaseProducerConfig producerConfig
            , String stream) {
        if (producerConfig instanceof ProducerConfig) {
            producer = axualClient.buildProducer((ProducerConfig) producerConfig);
        } else if (producerConfig instanceof GenericAvroProducerConfig) {
            producer = axualClient.buildProducer((GenericAvroProducerConfig) producerConfig);
        } else {
            throw new IllegalArgumentException("BaseProducerConfig type mismatch");
        }
        this.stream = stream;
    }

    public Future<ProducedMessage> produce(ProducerMessage message) {
        ProducerMessage streamAddedMessage = ProducerMessage.newBuilder()
                .setKey(message.getKey())
                .setValue(message.getValue())
                .setStream(stream)
                .build();

        Future<ProducedMessage> result = producer.produce(streamAddedMessage);
        return result;
    }

    @Override
    public void close() {
        producer.close();
    }
}
