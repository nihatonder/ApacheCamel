package com.learncamel.component.axual;

import io.axual.client.config.DeliveryStrategy;
import io.axual.client.config.GenericAvroProducerConfig;
import io.axual.client.config.OrderingStrategy;
import io.axual.client.config.ProducerConfig;
import io.axual.client.proxy.generic.registry.ProxyChain;
import io.axual.common.config.ClientConfig;
import io.axual.common.config.SslConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.StringSerializer;

import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.*;
import static io.axual.client.proxy.generic.registry.ProxyTypeRegistry.HEADER_PROXY_ID;

@Getter
@Setter
public class AxualConfig {

    //TODO: remove default values from all properties
    private String applicationId = "io.axual.example.client.avro.producer";
    private String applicationVersion = "0.0.1";
    private String endpoint = "http://127.0.0.1:8081";;
    private String tenant = "axual";
    private String environment = "local";

    private String keystoreLocation = getResourceFilePath("client-certs/axual.client.keystore.jks");
    private String keystorePassword = "notsecret";
    private String keyPassword = "notsecret";
    private String truststoreLocation = getResourceFilePath("client-certs/axual.client.truststore.jks");
    private String truststorePassword = "notsecret";

    public ClientConfig createClientConfig() {

        ClientConfig config = ClientConfig.newBuilder()
                .setApplicationId(applicationId)
                .setApplicationVersion(applicationVersion)
                .setEndpoint(endpoint)
                .setTenant(tenant)
                .setEnvironment(environment)
                .setSslConfig(
                        SslConfig.newBuilder()
                                .setEnableHostnameVerification(false)
                                .setKeystoreLocation(keystoreLocation)
                                .setKeystorePassword(keystorePassword)
                                .setKeyPassword(keyPassword)
                                .setTruststoreLocation(truststoreLocation)
                                .setTruststorePassword(truststorePassword)
                                .build()
                )
                .setDisableTemporarySecurityFile(false)//TODO:this has to be set to true before the cluster test
                .build();

        return config;
    }

    private static String getResourceFilePath(String resource) {
        return ClassLoader.getSystemClassLoader().getResource(resource).getFile();
    }
}
