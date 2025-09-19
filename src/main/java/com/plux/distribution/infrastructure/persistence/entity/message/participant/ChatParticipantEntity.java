package com.plux.distribution.infrastructure.persistence.entity.message.participant;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.participant.ChatParticipant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHAT")
public class ChatParticipantEntity extends ParticipantEntity {

    private Long chatId;

    @Override
    public Participant toModel() {
        return new ChatParticipant(new ChatId(chatId));
    }

    public static ChatParticipantEntity fromModel(ChatParticipant model) {
        var entity = new ChatParticipantEntity();

        entity.chatId = model.chatId().value();

        return entity;
    }
}
