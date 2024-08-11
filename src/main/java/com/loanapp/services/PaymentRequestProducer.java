package com.loanapp.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentRequestProducer {

    private static final String TOPIC = "payment_requested";

   // @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
