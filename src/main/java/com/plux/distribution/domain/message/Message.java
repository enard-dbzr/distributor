package com.plux.distribution.domain.message;

import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import com.plux.distribution.domain.message.state.MessageState;
import com.plux.distribution.domain.message.state.PendingState;
import com.plux.distribution.domain.message.state.TransferredState;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class Message {

    private final MessageId id;
    @NotNull
    private final Participant sender;
    @NotNull
    private Participant recipient;
    @NotNull
    private MessageState state;
    @NotNull
    private final MessageContent content;

    public Message(MessageId id, @NotNull Participant sender, @NotNull Participant recipient,
            @NotNull MessageState state,
            @NotNull MessageContent content) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.state = state;
        this.content = content;
    }

    public Message(@NotNull Participant sender, @NotNull Participant recipient, @NotNull MessageState state,
            @NotNull MessageContent content) {
        this(null, sender, recipient, state, content);
    }

    public Message(@NotNull Participant recipient, @NotNull MessageContent content) {
        this(null, new SelfParticipant(), recipient, new PendingState(), content);
    }

    public void markSent(@NotNull Date time) {
        this.state = new TransferredState(time);
    }

    public MessageId getId() {
        return id;
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
}
