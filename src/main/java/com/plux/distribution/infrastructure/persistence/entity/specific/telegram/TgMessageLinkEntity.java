package com.plux.distribution.infrastructure.persistence.entity.specific.telegram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tg_message_links")
public class TgMessageLinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SuppressWarnings("unused")
    private Long id;

    @Column(name = "interaction_id", nullable = false)
    private Long interactionId;

    @Column(name = "tg_message_id", nullable = false)
    private Integer tgMessageId;

    @Column(name = "tg_chat_id", nullable = false)
    private Long tgChatId;

    public TgMessageLinkEntity() {
    }

    public TgMessageLinkEntity(Long interactionId, Integer tgMessageId, Long tgChatId) {
        this.interactionId = interactionId;
        this.tgMessageId = tgMessageId;
        this.tgChatId = tgChatId;
    }

    public Long getInteractionId() {
        return interactionId;
    }

    public Integer getTgMessageId() {
        return tgMessageId;
    }

    public Long getTgChatId() {
        return tgChatId;
    }
}
