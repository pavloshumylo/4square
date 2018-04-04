package com.template.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = InformationDeserializer.class)
public class Information {

    private String id;
    private String name;
    private String address;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // 6. Mapping with JsonNode
    public Information jsonNodeCase(String json) throws IOException {
        JsonNode informationNode = new ObjectMapper().readTree(json);
        Information information = new Information();
        information.setId(informationNode.get("response").get("venues").get(0).get("id").textValue());
        information.setName(informationNode.get("response").get("venues").get(0).get("name").textValue());
        information.setAddress(informationNode.get("response").get("venues").get(0).get("location").get("address").textValue());
        information.setPhone(informationNode.get("response").get("venues").get(0).get("contact").get("phone").textValue());
        return information;
    }
}
