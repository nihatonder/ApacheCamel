package com.learncamel.component.axual;

import com.learncamel.enums.ProducerType;
import io.axual.client.config.BaseProducerConfig;
import io.axual.client.config.ProducerConfig;
import io.axual.common.config.ClientConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents axual endpoint.
 */
@Getter
@Setter
@UriEndpoint(scheme = "axual", title = "Axual", syntax = "axual:topic", label = "messaging")
public class AxualEndpoint extends DefaultEndpoint {
    private ClientConfig clientConfig;
    private BaseProducerConfig baseProducerConfig;

    private String stream;
    private ProducerType producerConfigType;

    public AxualEndpoint(String uri, AxualComponent component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        return new AxualProducer(this);
    }

    public Consumer createConsumer(Processor processor) {
        throw new UnsupportedOperationException("You can not read messages from this endpoint: " + getEndpointUri());
    }

    public boolean isSingleton() {
        return true;
    }
}
