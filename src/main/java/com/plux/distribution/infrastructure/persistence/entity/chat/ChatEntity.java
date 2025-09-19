package com.plux.distribution.infrastructure.persistence.entity.chat;

import com.plux.distribution.core.chat.domain.Chat;
import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.user.domain.UserId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chats")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    public ChatEntity(Chat model) {
        id = model.getId().value();
        userId = model.getUserId() == null ? null : model.getUserId().value();
    }

    public ChatEntity() {

    }

    public Chat toModel() {
        var resultUserId = userId == null ? null : new UserId(userId);
        return new Chat(new ChatId(id), resultUserId);
    }
}
