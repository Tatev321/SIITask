package com.example.siitask.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Map;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "timestamp")
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    @Column(name = "type")
    private String type;

    @Column(name = "actor")
    private String actor;

    @ElementCollection
    @CollectionTable(name = "transaction_data",
            joinColumns = @JoinColumn(name = "id"))
    @MapKeyColumn(name = "data_key")
    @Column(name = "transaction_value")
    private Map<String, String> transactionData;

    public Transaction() {
    }

    public Transaction(Timestamp timestamp, String type, String actor, Map<String, String> transactionData) {
        this.timestamp = timestamp;
        this.type = type;
        this.actor = actor;
        this.transactionData = transactionData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public Map<String, String> getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(Map<String, String> transactionData) {
        this.transactionData = transactionData;
    }
}
