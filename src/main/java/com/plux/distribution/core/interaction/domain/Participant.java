package com.plux.distribution.core.interaction.domain;

import com.plux.distribution.core.chat.domain.ChatId;
import org.jetbrains.annotations.NotNull;

public sealed interface Participant {

    record ChatParticipant(@NotNull ChatId chatId) implements Participant {}

    record BotParticipant() implements Participant {}
}
