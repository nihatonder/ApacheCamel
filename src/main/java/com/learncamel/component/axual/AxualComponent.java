package com.learncamel.component.axual;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;

import static com.learncamel.enums.ProducerType.GENERIC_AVRO_PRODUCER_CONFIG;
import static com.learncamel.enums.ProducerType.PRODUCER_CONFIG;

/**
 * Represents the component that manages {@link AxualEndpoint}.
 */
@Getter
@Setter
@Slf4j
public class AxualComponent extends DefaultComponent {
    private AxualConfig config = new AxualConfig(); //TODO:initialize from bean
    private AxualProducerConfig axualProducerConfig = new AxualProducerConfig<String, String>();//TODO:initialize from bean

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        log.info("AxualComponent is called uri: {} remaining: {} parameters.size: {}  ", uri, remaining, parameters != null ? parameters.size() : 0);
        //TODO:Remove when initialized from the bean
        axualProducerConfig.setKeySerializer(new StringSerializer());
        axualProducerConfig.setValueSerializer(new StringSerializer());

        AxualEndpoint endpoint = new AxualEndpoint(uri, this);
        setProperties(endpoint, parameters);

        endpoint.setClientConfig(config.createClientConfig());
        if(GENERIC_AVRO_PRODUCER_CONFIG.equals(endpoint.getProducerConfigType())) {
            endpoint.setBaseProducerConfig(axualProducerConfig.createGenericAvroProducerConfig());
        } else if (PRODUCER_CONFIG.equals(endpoint.getProducerConfigType())) {
            endpoint.setBaseProducerConfig(axualProducerConfig.createProducerConfig());
        }
        endpoint.setStream(remaining);

        return endpoint;
    }
}
