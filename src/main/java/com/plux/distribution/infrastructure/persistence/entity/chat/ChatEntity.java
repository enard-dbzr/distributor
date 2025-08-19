package com.plux.distribution.infrastructure.persistence.entity.chat;

import com.plux.distribution.domain.chat.Chat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "chats")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public static @NotNull ChatEntity fromModel(@NotNull Chat model) {
        return new ChatEntity();
    }

}
