package com.example.webserver;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class DataController {
    DataBaseModel dataBaseModel;

    @GetMapping("/time")
    public ResponseEntity<String> getTime() {
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return ResponseEntity.ok(formattedNow);
    }
    @GetMapping("/textall")
    public ResponseEntity<HashMap<String, String>> getTextAll() {
        Optional<HashMap<String, String>> texts = dataBaseModel.getTextAll();
        if (texts.isPresent() && !texts.get().isEmpty()) {
            return ResponseEntity.ok(texts.get());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/text/{id}")
    public ResponseEntity<String> getText(@PathVariable("id") String id) {
        Optional<String> text = dataBaseModel.getText(id);
        return text.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/text/{id}")
    public ResponseEntity<String> postText(@PathVariable("id") String id, @RequestBody String data) {
        Optional<String> message = dataBaseModel.putText(id, data);
        return message.map(m -> ResponseEntity.status(HttpStatus.CREATED).body(m))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Error occurred"));
    }

    @DeleteMapping("/text/{id}")
    public ResponseEntity<String> deleteText(@PathVariable("id") String id) {
        Optional<String> message = dataBaseModel.deleteText(id);
        return message.map(m -> ResponseEntity.status(HttpStatus.ACCEPTED).body(m))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_EXTENDED).build());
    }

}
