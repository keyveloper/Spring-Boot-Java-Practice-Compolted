package com.example.webserver;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "texts")
@Data
public class TextEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "text_num")
    private long textNum;

    @Column(name = "text_id", nullable = false)
    private String textId;

    @Column(name = "text_vale", nullable = false)
    private String textValue;
}
