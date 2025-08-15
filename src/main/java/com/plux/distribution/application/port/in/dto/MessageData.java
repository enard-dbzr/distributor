package com.plux.distribution.application.port.in.dto;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public record MessageData(@NotNull UserId userId, MessageId replyTo, @NotNull String text,
                          @NotNull Date timestamp) {

    public static MessageDataBuilder builder() {
        return new MessageDataBuilder();
    }

    public static class MessageDataBuilder {

        private UserId userId;
        private MessageId replyTo;
        private String text;
        private Date timestamp;

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

        public MessageDataBuilder setTimestamp(@NotNull Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MessageData build() {
            return new MessageData(userId, replyTo, text, timestamp);
        }
    }
}
