package com.learncamel.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
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

@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MyComponentTest extends CamelTestSupport {

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

    @Test
    public void testMy() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);
        
        assertMockEndpointsSatisfied();
    }

    @Test
    public void testWithBody() {
        String input = "{\"transactionType\":\"ADD\", \"sku\":\"200\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";
        String response = (String) producerTemplate.requestBody("mycomponent:foo",input);
        assertNotNull(response);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("mycomponent://foo")
                  .to("mycomponent://bar");
            }
        };
    }
}
