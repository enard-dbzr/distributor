package com.plux.distribution.infrastructure.persistence.entity.integration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "service_sendings")
public class ServiceSendingEntity {

    @Id
    private Long interactionId;

    @Column(nullable = false)
    private Long serviceId;

    public ServiceSendingEntity(Long interactionId, Long serviceId) {
        this.interactionId = interactionId;
        this.serviceId = serviceId;
    }

    public ServiceSendingEntity() {

    }

    public Long getInteractionId() {
        return interactionId;
    }

    public Long getServiceId() {
        return serviceId;
    }
}
