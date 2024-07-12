package com.example.webserver;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Getter
@Repository
@Slf4j
public class DataBaseModel {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Optional<String> putText(String key, String value) {
        try {
            TextEntity textEntity = new TextEntity();
            textEntity.setTextId(key);
            textEntity.setTextValue(value);
            entityManager.persist(textEntity);
            return Optional.of("Insertion successful");
        } catch (Exception e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<String> getText(String key) {
        try{
            TextEntity textEntity = entityManager
                    .createQuery("SELECT t FROM TextEntity t Where t.textId = :key", TextEntity.class)
                    .setParameter("key", key)
                    .getSingleResult();
            return Optional.ofNullable(textEntity.getTextValue());
        } catch (NoResultException e) {
            log.error("get Text result error" + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<HashMap<String, String>> getTextAll() {
        // return Json
        String sql = "SELECT * FROM texts";

        try {
            List<TextEntity> textEntities = entityManager
                    .createQuery("SELECT t From TextEntity t", TextEntity.class)
                    .getResultList();
            if (textEntities.isEmpty()) {
                return Optional.empty();
            }

            HashMap<String, String> textMap = new HashMap<>();
            for (TextEntity text : textEntities) {
                textMap.put(text.getTextId(), text.getTextValue());
            }

            return Optional.of(textMap);
        } catch (Exception e) {
            log.error("get TextAll result error" + e.getMessage());
            return Optional.empty();
        }
    }


    @Transactional
    public Optional<String> deleteText(String key) {
        try {
            TextEntity textEntity = entityManager
                    .createQuery("SELECT t From TextEntity t Where t.textId = :textId", TextEntity.class)
                    .setParameter("textId", key)
                    .getSingleResult();
            entityManager.remove(textEntity);
            return Optional.of("Key: " + key + " row was deleted!");
        } catch (Exception e) {
            // EntityManager 자체가 닫히면 모든 열려 있는 트랜잭션은 자동 롤백된다.
            log.error("DataBase Model: delete error: " + e.getMessage());
            return Optional.empty();
        }
    }
}
