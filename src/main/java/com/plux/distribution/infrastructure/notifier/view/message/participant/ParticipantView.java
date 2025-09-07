package com.plux.distribution.infrastructure.notifier.view.message.participant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.plux.distribution.domain.message.participant.ChatParticipant;
import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.participant.ParticipantVisitor;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import com.plux.distribution.domain.message.participant.ServiceParticipant;
import com.plux.distribution.domain.message.participant.UnknownServiceParticipant;
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
