package com.plux.distribution.infrastructure.notifier.view.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.interaction.domain.Participant;
import com.plux.distribution.core.interaction.domain.Participant.BotParticipant;
import com.plux.distribution.core.interaction.domain.Participant.ChatParticipant;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ParticipantView.ChatParticipantView.class, name = "chat"),
        @JsonSubTypes.Type(value = ParticipantView.BotParticipantView.class, name = "bot"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = ParticipantView.ChatParticipantView.class, value = "chat"),
                @DiscriminatorMapping(schema = ParticipantView.BotParticipantView.class, value = "bot"),
        }
)
public sealed interface ParticipantView {

    static @NotNull ParticipantView create(Participant participant) {
        return switch (participant) {
            case ChatParticipant p -> new ChatParticipantView(p.chatId().value());
            case BotParticipant _ -> new BotParticipantView();
        };
    }

    record BotParticipantView() implements ParticipantView {}

    record ChatParticipantView(@NotNull Long chatId) implements ParticipantView {}
}







