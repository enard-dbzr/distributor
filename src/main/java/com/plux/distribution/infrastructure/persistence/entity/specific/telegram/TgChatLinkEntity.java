package com.plux.distribution.infrastructure.persistence.entity.specific.telegram;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tg_chat_links")
public class TgChatLinkEntity {

    @Id
    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "tg_chat_id", nullable = false, unique = true)
    private Long tgChatId;

    public TgChatLinkEntity() {

    }

    public TgChatLinkEntity(Long chatId, Long tgChatId) {
        this.chatId = chatId;
        this.tgChatId = tgChatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public Long getTgChatId() {
        return tgChatId;
    }
}
