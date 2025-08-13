package com.plux.distribution.domain.message;

import com.plux.distribution.domain.message.content.MessageContent;
import com.plux.distribution.domain.message.participant.Participant;
import com.plux.distribution.domain.message.participant.SelfParticipant;
import com.plux.distribution.domain.message.state.MessageState;
import com.plux.distribution.domain.message.state.PendingState;
import com.plux.distribution.domain.message.state.TransferredState;
import java.util.Date;

public class Message {

    private final MessageId id;
    private final Participant sender;
    private Participant recipient;
    private MessageState state;
    private final MessageContent content;

    public Message(MessageId id, Participant sender, Participant recipient, MessageState state,
            MessageContent content) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.state = state;
        this.content = content;
    }

    public Message(Participant sender, Participant recipient, MessageState state, MessageContent content) {
        this(null, sender, recipient, state, content);
    }

    public Message(Participant recipient, MessageContent content) {
        this(null, new SelfParticipant(), recipient, new PendingState(), content);
    }

    public void markSent(Date time) {
        this.state = new TransferredState(time);
    }

    public MessageId getId() {
        return id;
    }

    public Participant getSender() {
        return sender;
    }

    public Participant getRecipient() {
        return recipient;
    }

    public void setRecipient(Participant recipient) {
        this.recipient = recipient;
    }

    public MessageState getState() {
        return state;
    }

    public MessageContent getContent() {
        return content;
    }
}
