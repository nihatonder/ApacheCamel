package com.learncamel.component.axual;

import io.axual.client.AxualClient;
import io.axual.client.config.DeliveryStrategy;
import io.axual.client.config.GenericAvroProducerConfig;
import io.axual.client.config.OrderingStrategy;
import io.axual.client.config.SpecificAvroProducerConfig;
import io.axual.client.example.schema.Application;
import io.axual.client.example.schema.ApplicationLogEvent;
import io.axual.client.producer.ProducedMessage;
import io.axual.client.producer.ProducerMessage;
import io.axual.client.proxy.generic.registry.ProxyChain;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.learncamel.util.utils.readExchangeBody;
import static io.axual.client.example.schema.ApplicationLogLevel.INFO;
import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.*;
import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.HEADER_PROXY_ID;
import static java.lang.Thread.sleep;

/**
 * Axual producer.
 */
public class AxualProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(AxualProducer.class);

    private AxualEndpoint endpoint;

    public AxualProducer(AxualEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        log.info("AxualProducer is called body: {} ", readExchangeBody(exchange));

        try (final AxualClient axualClient = new AxualClient(endpoint.getClientConfig());
             final BaseProducer producer = new BaseProducer(axualClient, endpoint.getBaseProducerConfig(), endpoint.getStream())) {
            Future<ProducedMessage> future = producer.produce((ProducerMessage) exchange.getIn().getBody());

            do {
                if (!future.isDone()) {
                    try {
                        ProducedMessage producedMessage = future.get();
                        exchange.getIn().setBody(producedMessage);
                        LOG.info("Produced message to topic {} partition {} offset {}", producedMessage.getStream(), producedMessage.getPartition(), producedMessage.getOffset());
                    } catch (InterruptedException | ExecutionException e) {
                        LOG.error("Error getting future, produce failed", e);
                    }
                }
                sleep(100);
            } while (!future.isDone());
        }
        LOG.info("Done!");
    }
}
