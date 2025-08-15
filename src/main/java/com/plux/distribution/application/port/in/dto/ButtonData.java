package com.plux.distribution.application.port.in.dto;

import com.plux.distribution.domain.message.MessageId;
import com.plux.distribution.domain.user.UserId;
import org.jetbrains.annotations.NotNull;

public record ButtonData(@NotNull UserId userId, @NotNull MessageId replyTo, @NotNull String tag) {

    public static ButtonDataBuilder builder() {
        return new ButtonDataBuilder();
    }

    public static class ButtonDataBuilder {

        private UserId userId;
        private MessageId replyTo;
        private String tag;

        public @NotNull ButtonDataBuilder setUserId(@NotNull UserId userId) {
            this.userId = userId;
            return this;
        }

        public @NotNull ButtonDataBuilder setReplyTo(@NotNull MessageId replyTo) {
            this.replyTo = replyTo;
            return this;
        }

        public @NotNull ButtonDataBuilder setTag(@NotNull String tag) {
            this.tag = tag;
            return this;
        }

        public @NotNull ButtonData build() {
            if (userId == null) {
                throw new IllegalArgumentException("userId is required");
            }
            if (replyTo == null) {
                throw new IllegalArgumentException("replyTo is required");
            }
            if (tag == null) {
                throw new IllegalArgumentException("tag is required");
            }

            return new ButtonData(userId, replyTo, tag);
        }
    }
}
