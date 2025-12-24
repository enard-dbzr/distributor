package com.plux.distribution.infrastructure.persistence.entity.interaction.participant;

import com.plux.distribution.core.chat.domain.ChatId;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHAT")
public class ChatParticipantEntity extends ParticipantEntity {

    private Long chatId;

    public ChatParticipantEntity(Long chatId) {
        this.chatId = chatId;
    }

    public ChatParticipantEntity() {
    }

    @Override
    public Participant toModel() {
        return new ChatParticipant(new ChatId(chatId));
    }
}
