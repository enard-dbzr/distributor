package com.plux.distribution.infrastructure.persistence.entity.message;

import com.plux.distribution.application.dto.message.CreateMessageCommand;
import com.plux.distribution.domain.message.Message;
import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.infrastructure.persistence.entity.message.content.MessageContentEntity;
import com.plux.distribution.infrastructure.persistence.entity.message.participant.ParticipantEntity;
import com.plux.distribution.infrastructure.persistence.entity.message.state.MessageStateEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private ParticipantEntity sender;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id")
    private ParticipantEntity recipient;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id")
    private MessageStateEntity state;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "content_id")
    private MessageContentEntity content;

    public MessageEntity(CreateMessageCommand command) {
        sender = ParticipantEntity.fromModel(command.sender());
        recipient = ParticipantEntity.fromModel(command.recipient());
        state = MessageStateEntity.fromModel(command.state());
        content = MessageContentEntity.fromModel(command.content());
    }

    public MessageEntity(Message model) {
        id = model.getId().value();
        sender = ParticipantEntity.fromModel(model.getSender());
        recipient = ParticipantEntity.fromModel(model.getRecipient());
        state = MessageStateEntity.fromModel(model.getState());
        content = MessageContentEntity.fromModel(model.getContent());
    }

    public MessageEntity() {

    }

    public Message toModel() {
        return new Message(
                new MessageId(id),
                sender.toModel(),
                recipient.toModel(),
                state.toModel(),
                content.toModel()
        );
    }

    public Long getId() {
        return id;
    }

    public void setRecipient(
            ParticipantEntity recipient) {
        this.recipient = recipient;
    }

    public void setState(
            MessageStateEntity state) {
        this.state = state;
    }

    public ParticipantEntity getRecipient() {
        return recipient;
    }

    public MessageStateEntity getState() {
        return state;
    }
}
