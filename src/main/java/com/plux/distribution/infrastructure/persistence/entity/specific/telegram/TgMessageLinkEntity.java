package com.plux.distribution.infrastructure.persistence.entity.specific.telegram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tg_message_links")
public class TgMessageLinkEntity {

    @Id
    @Column(name = "message_id", nullable = false, unique = true)
    private Long messageId;

    @Column(name = "tg_message_id", nullable = false)
    private Integer tgMessageId;

    @Column(name = "tg_chat_id", nullable = false)
    private Long tgChatId;

    public TgMessageLinkEntity() {
    }

    public TgMessageLinkEntity(Long messageId, Integer tgMessageId, Long tgChatId) {
        this.messageId = messageId;
        this.tgMessageId = tgMessageId;
        this.tgChatId = tgChatId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Integer getTgMessageId() {
        return tgMessageId;
    }

    public Long getTgChatId() {
        return tgChatId;
    }
}
