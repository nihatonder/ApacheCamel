package com.learncamel.component.axual;

import io.axual.client.config.DeliveryStrategy;
import io.axual.client.config.GenericAvroProducerConfig;
import io.axual.client.config.OrderingStrategy;
import io.axual.client.config.ProducerConfig;
import io.axual.client.proxy.generic.registry.ProxyChain;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.*;
import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.HEADER_PROXY_ID;

@Getter
@Setter
public class AxualProducerConfig<K,V> {

    private Serializer<K> keySerializer;
    private Serializer<V> valueSerializer;

    public GenericAvroProducerConfig createGenericAvroProducerConfig() {
        return
                GenericAvroProducerConfig.builder()
                        .setDeliveryStrategy(DeliveryStrategy.AT_LEAST_ONCE)
                        .setOrderingStrategy(OrderingStrategy.KEEPING_ORDER)
                        .setMessageBufferWaitTimeout(100)
                        .setBatchSize(1)
                        .setProxyChain(ProxyChain.newBuilder()
                                .append(SWITCHING_PROXY_ID)
                                .append(RESOLVING_PROXY_ID)
                                .append(LINEAGE_PROXY_ID)
                                .append(HEADER_PROXY_ID)
                                .build())
                        .build();
    }

    public ProducerConfig createProducerConfig() {
        return ProducerConfig.<K, V>builder()
                .setKeySerializer(keySerializer)
                .setValueSerializer(valueSerializer)
                .setDeliveryStrategy(DeliveryStrategy.AT_LEAST_ONCE)
                .setOrderingStrategy(OrderingStrategy.KEEPING_ORDER)
                .setMessageBufferWaitTimeout(100)
                .setBatchSize(1)
                .setProxyChain(ProxyChain.newBuilder()
                        .append(SWITCHING_PROXY_ID)
                        .append(RESOLVING_PROXY_ID)
                        .append(LINEAGE_PROXY_ID)
                        .append(HEADER_PROXY_ID)
                        .build())
                .build();
    }
}
