package com.plux.distribution.infrastructure.persistence.entity.workflow;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "frame_contexts")
public class FrameContextSnapshotEntity {

    @Id
    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "snapshot", nullable = false, columnDefinition = "TEXT")
    private String snapshot;

    public FrameContextSnapshotEntity() {

    }

    public FrameContextSnapshotEntity(Long chatId, String snapshot) {
        this.chatId = chatId;
        this.snapshot = snapshot;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getSnapshot() {
        return snapshot;
    }
}
