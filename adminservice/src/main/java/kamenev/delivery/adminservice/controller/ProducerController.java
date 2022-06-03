package kamenev.delivery.adminservice.controller;

import kamenev.delivery.adminservice.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    private final KafkaProducer kafkaProducer;

    @Autowired
    public ProducerController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/produce")
    public ResponseEntity<String> produce(@RequestBody String input) {
        kafkaProducer.send("topic1", input);
        return new ResponseEntity<>(input, HttpStatus.OK);
    }
}
