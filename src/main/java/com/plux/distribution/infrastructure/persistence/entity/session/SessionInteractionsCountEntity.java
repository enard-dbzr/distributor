package com.plux.distribution.infrastructure.persistence.entity.session;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "session_interactions")
public class SessionInteractionsCountEntity {
    @Id
    @SuppressWarnings("unused")
    private Long sessionId;

    private Integer count;

    public SessionInteractionsCountEntity(@NotNull Long sessionId, Integer count) {
        this.sessionId = sessionId;
        this.count = count;
    }

    public SessionInteractionsCountEntity() {

    }

    public Integer getCount() {
        return count;
    }
}
