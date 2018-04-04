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

    //related to 7.2
    @Override
    public Information deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode informationNode = jp.getCodec().readTree(jp);
        Information information = new Information();
        information.setId(informationNode.get("response").get("venues").get(0).get("id").textValue());
        information.setName(informationNode.get("response").get("venues").get(0).get("name").textValue());
        information.setAddress(informationNode.get("response").get("venues").get(0).get("location").get("address").textValue());
        information.setPhone(informationNode.get("response").get("venues").get(0).get("contact").get("phone").textValue());
        return information;
    }
}
