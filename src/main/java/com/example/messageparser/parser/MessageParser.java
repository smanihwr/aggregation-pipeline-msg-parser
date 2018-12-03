package com.example.messageparser.parser;


import com.example.messageparser.model.Message;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.util.List;

public class MessageParser {

    private static final Logger LOG = LoggerFactory.getLogger(MessageParser.class);

    public static void main(String[] args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        List<Message> messageList = mapper.readValue(
                new FileReader("src/main/resources/sampledata.json"), new TypeReference<List<Message>>() {
                });

        LOG.debug("Messages found: " + messageList.size());

        messageList.parallelStream()
                .forEach(message -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    HttpEntity<Message> request = new HttpEntity<>(message);

                            RestTemplate restTemplate = new RestTemplate();
                            String url = "http://localhost:8080/message-intake/api/message";
                            restTemplate.postForObject(url, request, Message.class);
                        }
                );
    }
}
