package com.plux.distribution.infrastructure.persistence.entity.integration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "integrations")
public class IntegrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String webhookUrl;

    @Column(nullable = false, unique = true)
    private String token;

    public IntegrationEntity(String webhookUrl, String token) {
        this.webhookUrl = webhookUrl;
        this.token = token;
    }

    public IntegrationEntity() {

    }

    public Long getId() {
        return id;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }
}
