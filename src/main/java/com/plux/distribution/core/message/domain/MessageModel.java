package com.plux.distribution.core.message.domain;

import com.plux.distribution.core.message.domain.content.MessageContent;
import com.plux.distribution.core.message.domain.participant.Participant;
import com.plux.distribution.core.message.domain.state.MessageState;
import com.plux.distribution.core.message.domain.state.TransferredState;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class MessageModel {

    private final @NotNull MessageId id;

    private final @NotNull Participant sender;

    private @NotNull Participant recipient;

    private @NotNull MessageState state;

    private final @NotNull MessageContent content;

    public MessageModel(@NotNull MessageId id, @NotNull Participant sender, @NotNull Participant recipient,
            @NotNull MessageState state,
            @NotNull MessageContent content) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.state = state;
        this.content = content;
    }

    public void markSent(@NotNull Date time) {
        this.state = new TransferredState(time);
    }

    @NotNull
    public Participant getSender() {
        return sender;
    }

    @NotNull
    public Participant getRecipient() {
        return recipient;
    }

    public void setRecipient(@NotNull Participant recipient) {
        this.recipient = recipient;
    }

    @NotNull
    public MessageState getState() {
        return state;
    }

    @NotNull
    public MessageContent getContent() {
        return content;
    }

    public @NotNull MessageId getId() {
        return id;
    }
}
