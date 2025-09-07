package com.plux.distribution.infrastructure.notifier.view.message.participant;

import org.jetbrains.annotations.NotNull;

public record ChatParticipantView(@NotNull Long chatId) implements ParticipantView {

}
