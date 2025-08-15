package com.plux.distribution.application.port.in.dto;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public record MessageData(@NotNull UserId userId, MessageId replyTo, @NotNull String text) {

    public static MessageDataBuilder builder() {
        return new MessageDataBuilder();
    }

    public static class MessageDataBuilder {

        private UserId userId;
        private MessageId replyTo;
        private String text;

        public MessageDataBuilder setUserId(@NotNull UserId userId) {
            this.userId = userId;
            return this;
        }

        public MessageDataBuilder setReplyTo(MessageId replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public MessageDataBuilder setText(@NotNull String text) {
            this.text = text;
            return this;
        }

        public MessageData build() {
            if (userId == null) {
                throw new IllegalArgumentException("userId is required");
            }
            if (text == null) {
                throw new IllegalArgumentException("text is required");
            }

            return new MessageData(userId, replyTo, text);
        }
    }
}
