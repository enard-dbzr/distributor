package com.plux.distribution.infrastructure.notifier.view.message.participant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.core.message.domain.participant.ChatParticipant;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.participant.ParticipantVisitor;
import com.plux.distribution.core.message.domain.participant.SelfParticipant;
import com.plux.distribution.core.message.domain.participant.ServiceParticipant;
import com.plux.distribution.core.message.domain.participant.UnknownServiceParticipant;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChatParticipantView.class, name = "chat"),
        @JsonSubTypes.Type(value = SelfParticipantView.class, name = "self"),
        @JsonSubTypes.Type(value = SelfParticipantView.class, name = "service"),
        @JsonSubTypes.Type(value = UnkServiceParticipantView.class, name = "unknown_service"),
})
@Schema(
        discriminatorMapping = {
                @DiscriminatorMapping(schema = ChatParticipantView.class, value = "chat"),
                @DiscriminatorMapping(schema = SelfParticipantView.class, value = "self"),
                @DiscriminatorMapping(schema = SelfParticipantView.class, value = "service"),
                @DiscriminatorMapping(schema = UnkServiceParticipantView.class, value = "unknown_service"),
        }
)
public sealed interface ParticipantView permits ChatParticipantView, SelfParticipantView, ServiceParticipantView,
        UnkServiceParticipantView {

    static @NotNull ParticipantView create(Participant participant) {
        return participant.accept(new ParticipantVisitor<>() {
            @Override
            public ParticipantView visit(ServiceParticipant participant) {
                return new ServiceParticipantView(participant.serviceId().value());
            }

            @Override
            public ParticipantView visit(UnknownServiceParticipant participant) {
                return new UnkServiceParticipantView();
            }

            @Override
            public ParticipantView visit(ChatParticipant participant) {
                return new ChatParticipantView(participant.chatId().value());
            }

            @Override
            public ParticipantView visit(SelfParticipant participant) {
                return new SelfParticipantView();
            }
        });
    }

}
