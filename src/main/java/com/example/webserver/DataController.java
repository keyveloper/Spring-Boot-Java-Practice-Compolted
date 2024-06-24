package com.example.webserver;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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
        HashMap<String, String> texts = dataBaseModel.getTextAll();
        if (texts != null && !texts.isEmpty()) {
            return ResponseEntity.ok(texts);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/text/{id}")
    public ResponseEntity<String> getText(@PathVariable("id") String id) {
        String text = dataBaseModel.getText(id);
        if (text != null) {
            return ResponseEntity.ok(text);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/text/{id}")
    public ResponseEntity<String> postText(@PathVariable("id") String id, @RequestBody String data) {
        boolean isSaved = dataBaseModel.putText(id, data);
        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Save successfully");
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Error occurred");
    }

    @DeleteMapping("/text/{id}")
    public ResponseEntity<String> deleteText(@PathVariable("id") String id) {
        boolean isDeleted = dataBaseModel.deleteText(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Delete Successfully");
        }
        return ResponseEntity.notFound().build();
    }

}
