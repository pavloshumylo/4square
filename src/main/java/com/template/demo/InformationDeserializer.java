package com.template.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class InformationDeserializer extends StdDeserializer<Information> {

    public InformationDeserializer() {
        this(null);
    }

    public InformationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Information deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode informationNode = jp.getCodec().readTree(jp);
        Information information = new Information();
        information.setPhone(informationNode.get("meta").get("requestId").textValue()); //test version
        return information;
    }
}
