package com.learncamel.routes;

import com.google.gson.Gson;
import com.learncamel.component.axual.AxualMessage;
import io.axual.client.config.DeliveryStrategy;
import io.axual.client.config.OrderingStrategy;
import io.axual.client.config.SpecificAvroProducerConfig;
import io.axual.client.example.schema.Application;
import io.axual.client.example.schema.ApplicationLogEvent;
import io.axual.client.producer.ProducedMessage;
import io.axual.client.producer.ProducerMessage;
import io.axual.client.proxy.generic.registry.ProxyChain;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Message;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.concurrent.Future;

import static io.axual.client.example.schema.ApplicationLogLevel.INFO;
import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.*;
import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.HEADER_PROXY_ID;

@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AxualComponentTest extends CamelTestSupport {

    @Autowired
    private CamelContext context;

    @Autowired
    protected CamelContext createCamelContext() {
        return context;
    }

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private ConsumerTemplate consumerTemplate;

    Gson gson = new Gson();

    @Test
    public void testMy() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testWithBody() {
        ProducerMessage message = createMessage();
        ProducedMessage response = (ProducedMessage) producerTemplate.requestBody("axualcomponent://avro-applicationlog?producerConfigType=GENERIC_AVRO_PRODUCER_CONFIG", message);
        assertNotNull(response);
    }

    @Test
    public void testStringMessage() {
        ProducerMessage message = createProducerStringMessage();
        ProducedMessage response = (ProducedMessage) producerTemplate.requestBody("axualcomponent://string-applicationlog?producerConfigType=PRODUCER_CONFIG", message);
        assertNotNull(response);
    }


    private ProducerMessage createMessage() {
        Application application = Application.newBuilder()
                .setName("TEST_APP")
                .setVersion("0.01")
                .build();
        Application key = Application.newBuilder()
                .setName("TEST_APP")
                .setVersion("1.9.9")
                .build();
        ApplicationLogEvent value = ApplicationLogEvent.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setSource(application)
                .setLevel(INFO)
                .setMessage("Component test to major stream")
                .setContext(Collections.singletonMap("Some key", "Some Value"))
                .build();

        return ProducerMessage.newBuilder()
                .setKey(key)
                .setValue(value)
                .setStream("")
                .build();
    }

    private ProducerMessage createProducerStringMessage() {
        Application application = Application.newBuilder()
                .setName("TEST_APP")
                .setVersion("0.01")
                .setOwner("Micheal Jackson")
                .build();
        Application key = Application.newBuilder()
                .setName("Test-string-app")
                .setVersion("1.9.9")
                .setOwner("none")
                .build();
        ApplicationLogEvent value = ApplicationLogEvent.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setSource(application)
                .setLevel(INFO)
                .setMessage("Component sends String")
                .setContext(Collections.singletonMap("Some key", "Some Value"))
                .build();

        return ProducerMessage.newBuilder()
                .setKey(key.toString())
                .setValue(value.toString())
                .setStream("")
                .build();
    }
}
