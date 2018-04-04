package com.template.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class Template {

    public static void main(String[] args) {
        SpringApplication.run(Template.class, args);

        try {
            String url = "https://api.foursquare.com/v2/venues/search?near=Lviv&client_id=E4WJZ5EZL2FP3LOGJVCWMMQDJOYB02JKJIMZ20DRZHYR5ZOT&client_secret=J0RZKSNJZH1ZHEGS2R0YJ5EDT4DS4CHDNMCBHIETS53CJS4Y&v=20180403";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity response = restTemplate.getForEntity(url, String.class);

            Information information = new Information();

            // 6. Mapping with JsonNode
            Information informationResult = information.jsonNodeCase(response.getBody().toString());

            System.out.println(informationResult.getId());
            System.out.println(informationResult.getName());
            System.out.println(informationResult.getAddress());
            System.out.println(informationResult.getPhone());

            // 7.2 Automatic Registration of Deserializer
            System.out.println();

            ObjectMapper mapper = new ObjectMapper();
            Information informationResultSecond = mapper.readValue(response.getBody().toString(), Information.class);

            System.out.println(informationResultSecond.getId());
            System.out.println(informationResultSecond.getName());
            System.out.println(informationResultSecond.getAddress());
            System.out.println(informationResultSecond.getPhone());
        }
        catch (HttpClientErrorException ex) {
            System.out.println(ex);
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

