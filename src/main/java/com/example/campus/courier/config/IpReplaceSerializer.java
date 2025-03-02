package com.example.campus.courier.config;

import com.example.campus.courier.common.annotation.IpReplace;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

@Component
public class IpReplaceSerializer extends JsonSerializer<String> implements ContextualSerializer {

    @Value("${server.port:8080}")
    private String serverPort;

    private String domainToReplace;
    private boolean enabled;

    public IpReplaceSerializer() {
        this.domainToReplace = "localhost:8080";
        this.enabled = false;
    }

    private IpReplaceSerializer(String domainToReplace, boolean enabled, String serverPort) {
        this.domainToReplace = domainToReplace;
        this.enabled = enabled;
        this.serverPort = serverPort;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (!enabled || value == null) {
            gen.writeString(value);
            return;
        }

        if (value.contains(domainToReplace)) {
            String localIp = InetAddress.getLocalHost().getHostAddress();
            String replacedValue = value.replace(domainToReplace, localIp + ":" + serverPort);
            gen.writeString(replacedValue);
        } else {
            gen.writeString(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        if (property == null) {
            return this;
        }

        IpReplace annotation = property.getAnnotation(IpReplace.class);
        if (annotation == null) {
            return this;
        }

        return new IpReplaceSerializer(annotation.value(), true, this.serverPort);
    }
} 