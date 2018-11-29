package com.example.messageparser.parser;


import com.example.messageparser.model.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.util.List;

public class MessageParser {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<Message> messageList = mapper.readValue(
                new FileReader("src/main/resources/sampledata.json"), new TypeReference<List<Message>>() {
                });

        System.out.println("Messages found: " + messageList.size());

        messageList.parallelStream()
                .forEach(message -> {
                            HttpEntity<Message> request = new HttpEntity<>(message);

                            RestTemplate restTemplate = new RestTemplate();
                            String url = "http://localhost:8080/message-intake/api/message";
                            restTemplate.postForObject(url, request, Message.class);
                        }
                );
    }
}
